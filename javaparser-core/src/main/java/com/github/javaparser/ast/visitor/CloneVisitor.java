/*
 * Copyright (C) 2007-2010 Júlio Vilmar Gesser.
 * Copyright (C) 2011, 2013-2016 The JavaParser Team.
 *
 * This file is part of JavaParser.
 * 
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License 
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
 
package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.imports.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;

import java.util.Optional;

import static com.github.javaparser.utils.Utils.option;

public class CloneVisitor implements GenericVisitor<Node, Object> {

	@Override
	public Node visit(CompilationUnit _n, Object _arg) {
		Optional<PackageDeclaration> package_ = cloneNode(_n.getPackage(), _arg);
		NodeList<ImportDeclaration> imports = cloneList(_n.getImports(), _arg);
		NodeList<TypeDeclaration<?>> types = cloneList(_n.getTypes(), _arg);

		return new CompilationUnit(
				_n.getRange(),
				package_, imports, types
		);
	}

	@Override
	public Node visit(PackageDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		NameExpr name = cloneNode(_n.getName(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		PackageDeclaration r = new PackageDeclaration(
				_n.getRange(),
				annotations, name
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(TypeParameter _n, Object _arg) {
        NodeList<ClassOrInterfaceType> typeBound = cloneList(_n.getTypeBound(), _arg);
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
        TypeParameter r = new TypeParameter(_n.getRange(),
                _n.getName(), typeBound, annotations);

        		Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(LineComment _n, Object _arg) {
		return new LineComment(_n.getRange(), _n.getContent());
	}

	@Override
	public Node visit(BlockComment _n, Object _arg) {
		return new BlockComment(_n.getRange(), _n.getContent());
	}

	@Override
	public Node visit(ClassOrInterfaceDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		NodeList<TypeParameter> typeParameters = cloneList(_n.getTypeParameters(), _arg);
		NodeList<ClassOrInterfaceType> extendsList = cloneList(_n.getExtends(), _arg);
		NodeList<ClassOrInterfaceType> implementsList = cloneList(_n.getImplements(), _arg);
		NodeList<BodyDeclaration<?>> members = cloneList(_n.getMembers(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        NameExpr nameExpr_ = cloneNode(_n.getNameExpr(), _arg);

        ClassOrInterfaceDeclaration r = new ClassOrInterfaceDeclaration(
				_n.getRange(),
				_n.getModifiers(), annotations, _n.isInterface(), nameExpr_, typeParameters, extendsList, implementsList, members
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(EnumDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		NodeList<ClassOrInterfaceType> implementsList = cloneList(_n.getImplements(), _arg);
        NodeList<EnumConstantDeclaration> entries = cloneList(_n.getEntries(), _arg);
		NodeList<BodyDeclaration<?>> members = cloneList(_n.getMembers(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        NameExpr nameExpr_ = cloneNode(_n.getNameExpr(), _arg);

		EnumDeclaration r = new EnumDeclaration(
				_n.getRange(),
				 _n.getModifiers(), annotations, nameExpr_, implementsList, entries, members
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(EmptyTypeDeclaration _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		EmptyTypeDeclaration r = new EmptyTypeDeclaration(
				_n.getRange()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(EnumConstantDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		NodeList<Expression> args = cloneList(_n.getArgs(), _arg);
		NodeList<BodyDeclaration<?>> classBody = cloneList(_n.getClassBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		EnumConstantDeclaration r = new EnumConstantDeclaration(
				_n.getRange(),
				 annotations, _n.getName(), args, classBody
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(AnnotationDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		NodeList<BodyDeclaration<?>> members = cloneList(_n.getMembers(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        NameExpr nameExpr_ = cloneNode(_n.getNameExpr(), _arg);

		AnnotationDeclaration r = new AnnotationDeclaration(
				_n.getRange(),
				 _n.getModifiers(), annotations, nameExpr_, members
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(AnnotationMemberDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		Type<?> type_ = cloneNode(_n.getType(), _arg);
		Optional<Expression> defaultValue = cloneNode(_n.getDefaultValue(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		AnnotationMemberDeclaration r = new AnnotationMemberDeclaration(
				_n.getRange(),
				 _n.getModifiers(), annotations, type_, _n.getName(), defaultValue
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(FieldDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations_ = cloneList(_n.getAnnotations(), _arg);
		Type<?> elementType_ = cloneNode(_n.getElementType(), _arg);
		NodeList<VariableDeclarator> variables_ = cloneList(_n.getVariables(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        NodeList<ArrayBracketPair> arrayBracketPairsAfterType_ = cloneList(_n.getArrayBracketPairsAfterElementType(), _arg);

        FieldDeclaration r = new FieldDeclaration(
				_n.getRange(),
				 _n.getModifiers(), 
                annotations_, 
                elementType_, 
                variables_,
                arrayBracketPairsAfterType_
                
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(VariableDeclarator _n, Object _arg) {
		VariableDeclaratorId id = cloneNode(_n.getId(), _arg);
		Optional<Expression> init = cloneNode(_n.getInit(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		VariableDeclarator r = new VariableDeclarator(
				_n.getRange(),
				id, init
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(VariableDeclaratorId _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		NodeList<ArrayBracketPair> arrayBracketPairsAfterId_ = cloneList(_n.getArrayBracketPairsAfterId(), _arg);

		VariableDeclaratorId r = new VariableDeclaratorId(
				_n.getRange(),
				_n.getName(),
				arrayBracketPairsAfterId_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ConstructorDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		NodeList<TypeParameter> typeParameters = cloneList(_n.getTypeParameters(), _arg);
		NodeList<Parameter> parameters= cloneList(_n.getParameters(), _arg);
        NodeList<ReferenceType<?>> throws_ = cloneList(_n.getThrows(), _arg);
		BlockStmt block = cloneNode(_n.getBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        NameExpr nameExpr_ = cloneNode(_n.getNameExpr(), _arg);

		ConstructorDeclaration r = new ConstructorDeclaration(
				_n.getRange(),
				 _n.getModifiers(), annotations, typeParameters, nameExpr_, parameters, throws_, block
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(MethodDeclaration _n, Object _arg) {
		NodeList<AnnotationExpr> annotations_ = cloneList(_n.getAnnotations(), _arg);
		NodeList<TypeParameter> typeParameters_ = cloneList(_n.getTypeParameters(), _arg);
		Type<?> type_ = cloneNode(_n.getElementType(), _arg);
        NameExpr nameExpr_ = cloneNode(_n.getNameExpr(), _arg);
		NodeList<Parameter> parameters_ = cloneList(_n.getParameters(), _arg);
        NodeList<ReferenceType<?>> throws_ = cloneList(_n.getThrows(), _arg);
        Optional<BlockStmt> block_ = cloneNode(_n.getBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		NodeList<ArrayBracketPair> arrayBracketPairsAfterElementType_ = cloneList(_n.getArrayBracketPairsAfterElementType(), _arg);
		NodeList<ArrayBracketPair> arrayBracketPairsAfterParameterList_ = cloneList(_n.getArrayBracketPairsAfterParameterList(), _arg);

		MethodDeclaration r = new MethodDeclaration(
				_n.getRange(),
				 _n.getModifiers(), 
                annotations_, 
                typeParameters_, 
                type_,
                arrayBracketPairsAfterElementType_,
                nameExpr_, 
                parameters_, 
                arrayBracketPairsAfterParameterList_, 
                throws_, 
                block_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(Parameter _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		Type<?> type_ = cloneNode(_n.getElementType(), _arg);
		VariableDeclaratorId id = cloneNode(_n.getId(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		NodeList<ArrayBracketPair> arrayBracketPairsAfterType_ = cloneList(_n.getArrayBracketPairsAfterElementType(), _arg);

        Parameter r = new Parameter(
				_n.getRange(),
				_n.getModifiers(), 
                annotations, 
                type_,
                arrayBracketPairsAfterType_,
                _n.isVarArgs(), 
                id
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(EmptyMemberDeclaration _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		EmptyMemberDeclaration r = new EmptyMemberDeclaration(
				_n.getRange()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(InitializerDeclaration _n, Object _arg) {
		BlockStmt block = cloneNode(_n.getBlock(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		InitializerDeclaration r = new InitializerDeclaration(
				_n.getRange(),
				 _n.isStatic(), block
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(JavadocComment _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		JavadocComment r = new JavadocComment(
				_n.getRange(),
				_n.getContent()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ClassOrInterfaceType _n, Object _arg) {
        Optional<ClassOrInterfaceType> scope = cloneNode(_n.getScope(), _arg);
        Optional<NodeList<Type<?>>> typeArguments = cloneList(_n.getTypeArguments(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ClassOrInterfaceType r = new ClassOrInterfaceType(
				_n.getRange(),
				scope,
				_n.getName(),
				typeArguments
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(PrimitiveType _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);

		PrimitiveType r = new PrimitiveType(
				_n.getRange(),
				_n.getType()
		);
		r.setComment(comment);
		r.setAnnotations(annotations);
		return r;
	}

	@Override
	public Node visit(ArrayType _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		Type<?> type_ = cloneNode(_n.getComponentType(), _arg);

		ArrayType r = new ArrayType(_n.getRange(), type_, annotations);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ArrayCreationLevel _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		Optional<Expression> dimension_ = cloneNode(_n.getDimension(), _arg);

		ArrayCreationLevel r = new ArrayCreationLevel(_n.getRange(), dimension_, annotations);

				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		r.setComment(comment);
		return r;
	}

	@Override
    public Node visit(IntersectionType _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
        NodeList<ReferenceType<?>> elements= cloneList(_n.getElements(), _arg);

        IntersectionType r = new IntersectionType(_n.getRange(), elements);
        		Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        r.setComment(comment);
		r.setAnnotations(annotations);
        return r;
    }

    @Override
    public Node visit(UnionType _n, Object _arg) {
	    NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
        NodeList<ReferenceType<?>> elements= cloneList(_n.getElements(), _arg);

        UnionType r = new UnionType(_n.getRange(), elements);
        		Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        r.setComment(comment);
		r.setAnnotations(annotations);
        return r;
    }

	@Override
	public Node visit(VoidType _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		VoidType r = new VoidType(_n.getRange());
		r.setAnnotations(annotations);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(WildcardType _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		Optional<ReferenceType<?>> ext = cloneNode(_n.getExtends(), _arg);
        Optional<ReferenceType<?>> sup = cloneNode(_n.getSuper(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		WildcardType r = new WildcardType(
				_n.getRange(),
				ext, sup
		);
		r.setComment(comment);
		r.setAnnotations(annotations);
		return r;
	}

	@Override
	public Node visit(UnknownType _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		UnknownType r = new UnknownType();
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ArrayAccessExpr _n, Object _arg) {
		Expression name = cloneNode(_n.getName(), _arg);
		Expression index = cloneNode(_n.getIndex(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ArrayAccessExpr r = new ArrayAccessExpr(
				_n.getRange(),
				name, index
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ArrayCreationExpr _n, Object _arg) {
		Type<?> type_ = cloneNode(_n.getElementType(), _arg);
        NodeList<ArrayCreationLevel> levels_ = cloneList(_n.getLevels(), _arg);
		Optional<ArrayInitializerExpr> initializer_ = cloneNode(_n.getInitializer(), _arg);

		ArrayCreationExpr r = new ArrayCreationExpr(_n.getRange(), type_, levels_, initializer_);

				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
        r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ArrayInitializerExpr _n, Object _arg) {
        NodeList<Expression> values = cloneList(_n.getValues(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ArrayInitializerExpr r = new ArrayInitializerExpr(
				_n.getRange(),
				values
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(AssignExpr _n, Object _arg) {
		Expression target = cloneNode(_n.getTarget(), _arg);
		Expression value = cloneNode(_n.getValue(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		AssignExpr r = new AssignExpr(
				_n.getRange(),
				target, value, _n.getOperator());
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(BinaryExpr _n, Object _arg) {
		Expression left = cloneNode(_n.getLeft(), _arg);
		Expression right = cloneNode(_n.getRight(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		BinaryExpr r = new BinaryExpr(
				_n.getRange(),
				left, right, _n.getOperator()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(CastExpr _n, Object _arg) {
		Type<?> type_ = cloneNode(_n.getType(), _arg);
		Expression expr = cloneNode(_n.getExpr(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		CastExpr r = new CastExpr(
				_n.getRange(),
				type_, expr
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ClassExpr _n, Object _arg) {
		Type<?> type_ = cloneNode(_n.getType(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ClassExpr r = new ClassExpr(
				_n.getRange(),
				type_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ConditionalExpr _n, Object _arg) {
		Expression condition = cloneNode(_n.getCondition(), _arg);
		Expression thenExpr = cloneNode(_n.getThenExpr(), _arg);
		Expression elseExpr = cloneNode(_n.getElseExpr(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ConditionalExpr r = new ConditionalExpr(
				_n.getRange(),
				condition, thenExpr, elseExpr
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(EnclosedExpr _n, Object _arg) {
		Optional<Expression> inner = cloneNode(_n.getInner(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		EnclosedExpr r = new EnclosedExpr(
				_n.getRange(),
				inner
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(FieldAccessExpr _n, Object _arg) {
		Expression scope_ = cloneNode(_n.getScope(), _arg);
        Optional<NodeList<Type<?>>> typeArguments_ = cloneList(_n.getTypeArguments(), _arg);
        NameExpr fieldExpr_ = cloneNode(_n.getFieldExpr(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		FieldAccessExpr r = new FieldAccessExpr(
				_n.getRange(),
				scope_, 
                typeArguments_, 
                fieldExpr_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(InstanceOfExpr _n, Object _arg) {
		Expression expr = cloneNode(_n.getExpr(), _arg);
		ReferenceType<?> type_ = cloneNode(_n.getType(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		InstanceOfExpr r = new InstanceOfExpr(
				_n.getRange(),
				expr, type_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(StringLiteralExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		StringLiteralExpr r = new StringLiteralExpr(
				_n.getRange(),
				_n.getValue()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(IntegerLiteralExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		IntegerLiteralExpr r = new IntegerLiteralExpr(
				_n.getRange(),
				_n.getValue()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(LongLiteralExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		LongLiteralExpr r = new LongLiteralExpr(
				_n.getRange(),
				_n.getValue()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(IntegerLiteralMinValueExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		IntegerLiteralMinValueExpr r = new IntegerLiteralMinValueExpr(_n.getRange());
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(LongLiteralMinValueExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		LongLiteralMinValueExpr r = new LongLiteralMinValueExpr(_n.getRange());
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(CharLiteralExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		CharLiteralExpr r = new CharLiteralExpr(
				_n.getRange(),
				_n.getValue()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(DoubleLiteralExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		DoubleLiteralExpr r = new DoubleLiteralExpr(
				_n.getRange(),
				_n.getValue()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(BooleanLiteralExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		BooleanLiteralExpr r = new BooleanLiteralExpr(
				_n.getRange(),
				_n.getValue()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(NullLiteralExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		NullLiteralExpr r = new NullLiteralExpr(_n.getRange());
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(MethodCallExpr _n, Object _arg) {
        Optional<Expression> scope_ = cloneNode(_n.getScope(), _arg);
        Optional<NodeList<Type<?>>> typeArguments_ = cloneList(_n.getTypeArguments(), _arg);
        NodeList<Expression> args = cloneList(_n.getArgs(), _arg);
        NameExpr nameExpr = cloneNode(_n.getNameExpr(), _arg);
        		Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		MethodCallExpr r = new MethodCallExpr(
				_n.getRange(),
				scope_, 
                typeArguments_, 
                nameExpr,
                args
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(NameExpr _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		NameExpr r = new NameExpr(
				_n.getRange(),
				_n.getName()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ObjectCreationExpr _n, Object _arg) {
        Optional<Expression> scope = cloneNode(_n.getScope(), _arg);
		ClassOrInterfaceType type_ = cloneNode(_n.getType(), _arg);
        Optional<NodeList<Type<?>>> typeArguments = cloneList(_n.getTypeArguments(), _arg);
        NodeList<Expression> args = cloneList(_n.getArgs(), _arg);
        Optional<NodeList<BodyDeclaration<?>>> anonymousBody = cloneList(_n.getAnonymousClassBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ObjectCreationExpr r = new ObjectCreationExpr(
				_n.getRange(),
				scope, type_, typeArguments, args, anonymousBody
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(QualifiedNameExpr _n, Object _arg) {
		NameExpr scope = cloneNode(_n.getQualifier(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		QualifiedNameExpr r = new QualifiedNameExpr(
				_n.getRange(),
				scope, _n.getName()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ThisExpr _n, Object _arg) {
        Optional<Expression> classExpr = cloneNode(_n.getClassExpr(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ThisExpr r = new ThisExpr(
				_n.getRange(),
				classExpr
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(SuperExpr _n, Object _arg) {
        Optional<Expression> classExpr = cloneNode(_n.getClassExpr(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		SuperExpr r = new SuperExpr(
				_n.getRange(),
				classExpr
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(UnaryExpr _n, Object _arg) {
		Expression expr = cloneNode(_n.getExpr(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		UnaryExpr r = new UnaryExpr(
				_n.getRange(),
				expr, _n.getOperator()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(VariableDeclarationExpr _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);
		Type<?> type_ = cloneNode(_n.getElementType(), _arg);
		NodeList<VariableDeclarator> variables_ = cloneList(_n.getVariables(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		NodeList<ArrayBracketPair> arrayBracketPairsAfterType_ = cloneList(_n.getArrayBracketPairsAfterElementType(), _arg);

		VariableDeclarationExpr r = new VariableDeclarationExpr(
				_n.getRange(),
				_n.getModifiers(), 
                annotations, 
                type_, 
                variables_,
                arrayBracketPairsAfterType_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(MarkerAnnotationExpr _n, Object _arg) {
		NameExpr name = cloneNode(_n.getName(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		MarkerAnnotationExpr r = new MarkerAnnotationExpr(
				_n.getRange(),
				name
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(SingleMemberAnnotationExpr _n, Object _arg) {
		NameExpr name = cloneNode(_n.getName(), _arg);
		Expression memberValue = cloneNode(_n.getMemberValue(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		SingleMemberAnnotationExpr r = new SingleMemberAnnotationExpr(
				_n.getRange(),
				name, memberValue
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(NormalAnnotationExpr _n, Object _arg) {
		NameExpr name = cloneNode(_n.getName(), _arg);
        NodeList<MemberValuePair> pairs = cloneList(_n.getPairs(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		NormalAnnotationExpr r = new NormalAnnotationExpr(
				_n.getRange(),
				name, pairs
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(MemberValuePair _n, Object _arg) {
		Expression value = cloneNode(_n.getValue(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		MemberValuePair r = new MemberValuePair(
				_n.getRange(),
				_n.getName(), value
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ExplicitConstructorInvocationStmt _n, Object _arg) {
        Optional<NodeList<Type<?>>> typeArguments_ = cloneList(_n.getTypeArguments(), _arg);
		Optional<Expression> expr_ = cloneNode(_n.getExpr(), _arg);
        NodeList<Expression> args = cloneList(_n.getArgs(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ExplicitConstructorInvocationStmt r = new ExplicitConstructorInvocationStmt(
				_n.getRange(),
				typeArguments_, 
                _n.isThis(), 
                expr_, 
                args
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(TypeDeclarationStmt _n, Object _arg) {
        TypeDeclaration<?> typeDecl = cloneNode(_n.getTypeDeclaration(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		TypeDeclarationStmt r = new TypeDeclarationStmt(
				_n.getRange(),
				typeDecl
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(AssertStmt _n, Object _arg) {
		Expression check = cloneNode(_n.getCheck(), _arg);
        Optional<Expression> message = cloneNode(_n.getMessage(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		AssertStmt r = new AssertStmt(
				_n.getRange(),
				check, message
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(BlockStmt _n, Object _arg) {
		NodeList<Statement> stmts = cloneList(_n.getStmts(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		BlockStmt r = new BlockStmt(
				_n.getRange(),
				stmts
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(LabeledStmt _n, Object _arg) {
		Statement stmt = cloneNode(_n.getStmt(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		LabeledStmt r = new LabeledStmt(
				_n.getRange(),
				_n.getLabel(), stmt
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(EmptyStmt _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		EmptyStmt r = new EmptyStmt(_n.getRange());
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ExpressionStmt _n, Object _arg) {
		Expression expr = cloneNode(_n.getExpression(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ExpressionStmt r = new ExpressionStmt(
				_n.getRange(),
				expr
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(SwitchStmt _n, Object _arg) {
		Expression selector = cloneNode(_n.getSelector(), _arg);
        NodeList<SwitchEntryStmt> entries = cloneList(_n.getEntries(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		SwitchStmt r = new SwitchStmt(
				_n.getRange(),
				selector, entries
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(SwitchEntryStmt _n, Object _arg) {
        Optional<Expression> label = cloneNode(_n.getLabel(), _arg);
		NodeList<Statement> stmts = cloneList(_n.getStmts(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		SwitchEntryStmt r = new SwitchEntryStmt(
				_n.getRange(),
				label, stmts
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(BreakStmt _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		BreakStmt r = new BreakStmt(
				_n.getRange(),
				_n.getId()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ReturnStmt _n, Object _arg) {
		Optional<Expression> expr = cloneNode(_n.getExpr(), _arg);
		Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg);

		ReturnStmt r = new ReturnStmt(
				_n.getRange(),
				expr
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(IfStmt _n, Object _arg) {
		Expression condition = cloneNode(_n.getCondition(), _arg);
		Statement thenStmt = cloneNode(_n.getThenStmt(), _arg);
        Optional<Statement> elseStmt = cloneNode(_n.getElseStmt(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		IfStmt r = new IfStmt(
				_n.getRange(),
				condition, thenStmt, elseStmt
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(WhileStmt _n, Object _arg) {
		Expression condition = cloneNode(_n.getCondition(), _arg);
		Statement body = cloneNode(_n.getBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		WhileStmt r = new WhileStmt(
				_n.getRange(),
				condition, body
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ContinueStmt _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ContinueStmt r = new ContinueStmt(
				_n.getRange(),
				_n.getId()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(DoStmt _n, Object _arg) {
		Statement body = cloneNode(_n.getBody(), _arg);
		Expression condition = cloneNode(_n.getCondition(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		DoStmt r = new DoStmt(
				_n.getRange(),
				body, condition
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ForeachStmt _n, Object _arg) {
		VariableDeclarationExpr var = cloneNode(_n.getVariable(), _arg);
		Expression iterable = cloneNode(_n.getIterable(), _arg);
		Statement body = cloneNode(_n.getBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ForeachStmt r = new ForeachStmt(
				_n.getRange(),
				var, iterable, body
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ForStmt _n, Object _arg) {
		NodeList<Expression> init = cloneList(_n.getInit(), _arg);
        Optional<Expression> compare = cloneNode(_n.getCompare(), _arg);
		NodeList<Expression> update = cloneList(_n.getUpdate(), _arg);
		Statement body = cloneNode(_n.getBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ForStmt r = new ForStmt(
				_n.getRange(),
				init, compare, update, body
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(ThrowStmt _n, Object _arg) {
		Expression expr = cloneNode(_n.getExpr(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		ThrowStmt r = new ThrowStmt(
				_n.getRange(),
				expr
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(SynchronizedStmt _n, Object _arg) {
		Expression expr = cloneNode(_n.getExpr(), _arg);
		BlockStmt block = cloneNode(_n.getBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		SynchronizedStmt r = new SynchronizedStmt(
				_n.getRange(),
				expr, block
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(TryStmt _n, Object _arg) {
		NodeList<VariableDeclarationExpr> resources = cloneList(_n.getResources(),_arg);
		BlockStmt tryBlock = cloneNode(_n.getTryBlock(), _arg);
		NodeList<CatchClause> catchs = cloneList(_n.getCatchs(), _arg);
        Optional<BlockStmt> finallyBlock = cloneNode(_n.getFinallyBlock(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		TryStmt r = new TryStmt(
				_n.getRange(),
				resources, tryBlock, catchs, finallyBlock
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(CatchClause _n, Object _arg) {
		Parameter param = cloneNode(_n.getParam(), _arg);
		BlockStmt catchBlock = cloneNode(_n.getBody(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		CatchClause r = new CatchClause(_n.getRange(), param, catchBlock);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(LambdaExpr _n, Object _arg) {
		NodeList<Parameter> lambdaParameters = cloneList(_n.getParameters(), _arg);

		Statement body = cloneNode(_n.getBody(), _arg);

		return new LambdaExpr(_n.getRange(), lambdaParameters, body,
				_n.isParametersEnclosed());
	}

	@Override
	public Node visit(MethodReferenceExpr _n, Object arg) {

		Expression scope = cloneNode(_n.getScope(), arg);
        Optional<NodeList<Type<?>>> typeArguments = cloneList(_n.getTypeArguments(), arg);

		return new MethodReferenceExpr(_n.getRange(), scope,
				typeArguments, _n.getIdentifier());
	}

	@Override
	public Node visit(TypeExpr n, Object arg) {

		Type<?> t = cloneNode(n.getType(), arg);

		return new TypeExpr(n.getRange(), t);
	}

	@Override
	public Node visit(ArrayBracketPair _n, Object _arg) {
		NodeList<AnnotationExpr> annotations = cloneList(_n.getAnnotations(), _arg);

		return new ArrayBracketPair(_n.getRange(), annotations);
	}

	@Override
	public Node visit(NodeList n, Object arg) {
		NodeList<Node> newNodes = new NodeList<>(n.getRange(), null);
		for (Object node : n) {
			Node resultNode = ((Node)node).accept(this, arg);
			if(resultNode!=null){
				newNodes.add(resultNode);
			}
		}
		return newNodes;
	}

	@Override
	public Node visit(EmptyImportDeclaration _n, Object _arg) {
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 
		return new EmptyImportDeclaration(_n.getRange()).setComment(comment);
	}

	@Override
	public Node visit(SingleStaticImportDeclaration _n, Object _arg) {
		ClassOrInterfaceType type_ = cloneNode(_n.getType(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 


		SingleStaticImportDeclaration r = new SingleStaticImportDeclaration(
				_n.getRange(),
				type_,
				_n.getStaticMember()
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(SingleTypeImportDeclaration _n, Object _arg) {
        ClassOrInterfaceType type_ = cloneNode(_n.getType(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		SingleTypeImportDeclaration r = new SingleTypeImportDeclaration(
				_n.getRange(),
                type_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(StaticImportOnDemandDeclaration _n, Object _arg) {
        ClassOrInterfaceType type_ = cloneNode(_n.getType(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		StaticImportOnDemandDeclaration r = new StaticImportOnDemandDeclaration(
				_n.getRange(),
                type_
		);
		r.setComment(comment);
		return r;
	}

	@Override
	public Node visit(TypeImportOnDemandDeclaration _n, Object _arg) {
		NameExpr name = cloneNode(_n.getName(), _arg);
				Optional<? extends Comment> comment = cloneNode(_n.getComment(), _arg); 

		TypeImportOnDemandDeclaration r = new TypeImportOnDemandDeclaration(
				_n.getRange(),
				name
		);
		r.setComment(comment);
		return r;
	}

    @SuppressWarnings("unchecked")
    protected <T extends Node> T cloneNode(T _node, Object _arg) {
        if (_node == null)
            return null;
        Node r = _node.accept(this, _arg);
        if (r == null)
            return null;
        return (T) r;
    }

    protected <N extends Node> Optional<N> cloneNode(Optional<N> optionalNode, Object arg) {
        return optionalNode.flatMap(n -> option(cloneNode(n, arg)));
    }


    private <N extends Node> NodeList<N> cloneList(NodeList<N> list, Object _arg) {
        if (list == null) {
            return null;
        }
        return (NodeList<N>) list.accept(this, _arg);
    }

    private <N extends Node> Optional<NodeList<N>> cloneList(Optional<NodeList<N>> optionalList, Object _arg) {
        return optionalList.flatMap(l -> option((NodeList<N>) l.accept(this, _arg)));
    }

}
