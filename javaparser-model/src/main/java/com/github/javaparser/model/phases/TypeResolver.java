package com.github.javaparser.model.phases;

import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.model.Registry;
import com.github.javaparser.model.classpath.Classpath;
import com.github.javaparser.model.element.TypeElem;
import com.github.javaparser.model.element.TypeParameterElem;
import com.github.javaparser.model.scope.EltNames;
import com.github.javaparser.model.scope.EltSimpleName;
import com.github.javaparser.model.scope.Scope;
import com.github.javaparser.model.scope.ScopeException;
import com.github.javaparser.model.source.SourceOrigin;
import com.github.javaparser.model.type.*;

import javax.lang.model.type.TypeMirror;
import java.util.*;

import static com.github.javaparser.model.source.utils.NodeListUtils.visitAll;

/**
 * @author Didier Villevalois
 */
public class TypeResolver implements Registry.Participant {

	private Classpath classpath;
	private TypeUtils typeUtils;

	@Override
	public void configure(Registry registry) {
		classpath = registry.get(Classpath.class);
		typeUtils = registry.get(TypeUtils.class);
	}

	public List<TypeMirror> resolveTypes(List<? extends Type> types, Scope scope) {
		List<TypeMirror> tpeMirrors = new ArrayList<TypeMirror>();
		if (types != null) {
			for (Type type : types) {
				tpeMirrors.add(resolveType(type, scope));
			}
		}
		return tpeMirrors;
	}

	public TypeMirror resolveType(Type type, Scope scope) {
		TypeMirror tpeMirror = type.accept(typeResolver, scope);
		if (tpeMirror == null) {
			throw new ScopeException("Can't resolve type '" + type + "'", null);
		}
		return tpeMirror;
	}

	public void resolveTypeParameters(List<TypeParameterElem> typeParameters, Scope scope) {
		CycleDetectingTypeVisitor cdtResolver = new CycleDetectingTypeVisitor();
		for (TypeParameterElem typeParameterElem : typeParameters) {
			cdtResolver.resolveBounds(typeParameterElem, scope);
		}
	}

	private TypeElem findTypeElem(EltSimpleName typeName, Scope scope) {
		TypeElem typeElem = scope.resolveType(typeName);
		if (typeElem == null) {
			throw new ScopeException("Can't find type '" + typeName + "'", null);
		}
		return typeElem;
	}

	private TypeVisitor typeResolver = new TypeVisitor();

	class TypeVisitor extends GenericVisitorAdapter<TypeMirror, Scope> {

		protected List<TypeMirror> resolveBounds(TypeParameterElem typeParameterElem, Scope scope) {
			List<TypeMirror> boundsMirrors = typeParameterElem.getBounds();
			if (boundsMirrors != null) return boundsMirrors;
			else return Collections.emptyList();
		}

		@Override
		public TpeMirror visit(ClassOrInterfaceType n, Scope arg) {
			ClassOrInterfaceType typeScope = n.getScope();
			EltSimpleName typeName = EltNames.makeSimple(n.getName());
			List<Type> typeArgs = n.getTypeArgs();

			// TODO Add consistency checks
			// - Static/Instance type members
			// - No scope and no type args on type vars

			TypeParameterElem typeParameterElem = arg.resolveTypeParameter(typeName);
			if (typeParameterElem != null) {
				List<TypeMirror> bounds = resolveBounds(typeParameterElem, arg);
				if (bounds.isEmpty())
					return new TpeVariable(typeParameterElem, typeUtils.objectType(), NullTpe.NULL);
				else return new TpeVariable(typeParameterElem, new IntersectionTpe(bounds), NullTpe.NULL);
			} else {
				List<TypeMirror> tpeArgsMirrors = visitAll(this, arg, typeArgs);

				if (typeScope != null) {
					DeclaredTpe typeScopeMirror = (DeclaredTpe) typeScope.accept(this, arg);
					TypeElem typeScopeElem = typeScopeMirror.asElement();
					TypeElem typeElem = findTypeElem(typeName, typeScopeElem.scope());
					return new DeclaredTpe(typeScopeMirror, typeElem, tpeArgsMirrors);
				} else {
					TypeElem typeElem = findTypeElem(typeName, arg);
					return new DeclaredTpe(NoTpe.NONE, typeElem, tpeArgsMirrors);
				}
			}
		}

		@Override
		public TypeMirror visit(ReferenceType n, Scope arg) {
			int depth = n.getArrayCount();
			Type type = n.getType();
			TypeMirror tpeMirror = type.accept(this, arg);
			return makeArray(tpeMirror, depth);
		}

		private TypeMirror makeArray(TypeMirror tpeMirror, int depth) {
			if (depth == 0) return tpeMirror;
			else return makeArray(new ArrayTpe(tpeMirror), depth);
		}

		@Override
		public TypeMirror visit(WildcardType n, Scope arg) {
			ReferenceType eBound = n.getExtends();
			ReferenceType sBound = n.getSuper();

			TypeMirror eBoundMirror = eBound != null ? eBound.accept(this, arg) : typeUtils.objectType();
			TypeMirror sBoundMirror = sBound != null ? sBound.accept(this, arg) : NullTpe.NULL;
			return new WildcardTpe(eBoundMirror, sBoundMirror);
		}

		@Override
		public TypeMirror visit(PrimitiveType n, Scope arg) {
			switch (n.getType()) {
				case Boolean:
					return PrimitiveTpe.BOOLEAN;
				case Char:
					return PrimitiveTpe.CHAR;
				case Byte:
					return PrimitiveTpe.BYTE;
				case Short:
					return PrimitiveTpe.SHORT;
				case Int:
					return PrimitiveTpe.INT;
				case Long:
					return PrimitiveTpe.LONG;
				case Float:
					return PrimitiveTpe.FLOAT;
				case Double:
					return PrimitiveTpe.DOUBLE;
				default:
					throw new IllegalArgumentException();
			}
		}

		@Override
		public TypeMirror visit(VoidType n, Scope arg) {
			return NoTpe.VOID;
		}
	}

	class CycleDetectingTypeVisitor extends TypeVisitor {

		Map<TypeParameterElem, Object> pendingResolution = new IdentityHashMap<TypeParameterElem, Object>();

		@Override
		public List<TypeMirror> resolveBounds(TypeParameterElem typeParameterElem, Scope scope) {
			List<TypeMirror> boundsMirrors = typeParameterElem.getBounds();
			if (boundsMirrors == null) {
				SourceOrigin origin = (SourceOrigin) typeParameterElem.origin();
				TypeParameter node = (TypeParameter) origin.getNode();
				List<ClassOrInterfaceType> bounds = node.getTypeBound();

				if (pendingResolution.containsKey(typeParameterElem))
					throw new ScopeException("Circular type parameter definition", node);

				pendingResolution.put(typeParameterElem, new Object());
				boundsMirrors = visitAll(this, scope, bounds);
				typeParameterElem.setBounds(boundsMirrors);
				pendingResolution.remove(typeParameterElem);
			}
			return boundsMirrors;
		}
	}
}
