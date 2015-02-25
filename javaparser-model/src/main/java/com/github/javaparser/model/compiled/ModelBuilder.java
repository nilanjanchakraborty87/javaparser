package com.github.javaparser.model.compiled;

import com.github.javaparser.model.ClassRegistry;
import com.github.javaparser.model.classpath.ClasspathElement;
import com.github.javaparser.model.element.Elem;
import com.github.javaparser.model.element.Origin;
import com.github.javaparser.model.element.TypeElem;
import com.github.javaparser.model.element.TypeParameterElem;
import com.github.javaparser.model.scope.EltName;
import com.github.javaparser.model.scope.EltNames;
import com.github.javaparser.model.scope.EltSimpleName;
import com.github.javaparser.model.scope.Scope;
import com.github.javaparser.model.type.TpeMirror;
import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javassist.ClassPool;
import javassist.CtClass;
import org.objectweb.asm.ClassReader;

import javax.lang.model.element.*;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Federico Tomassetti
 */
public class ModelBuilder {
    
    private class ProxyForClassToSolve extends TypeElem {

        @Override
        public Origin origin() {
            return super.origin();
        }

        @Override
        public Scope parentScope() {
            return super.parentScope();
        }

        @Override
        public Elem getEnclosingElement() {
            return super.getEnclosingElement();
        }

        @Override
        public Set<Modifier> getModifiers() {
            return super.getModifiers();
        }

        @Override
        public EltSimpleName getSimpleName() {
            Optional<TypeElem> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                return solved.get().getSimpleName();
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public ElementKind getKind() {
            return super.getKind();
        }

        @Override
        public List<Elem> getEnclosedElements() {
            Optional<TypeElem> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                return solved.get().getEnclosedElements();
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public void addEnclosedElem(Elem elem) {
           enclosedRegistry.record(this.name, elem);
        }

        @Override
        public EltName getQualifiedName() {
            Optional<TypeElem> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                return solved.get().getQualifiedName();
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public NestingKind getNestingKind() {
            Optional<TypeElem> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                return solved.get().getNestingKind();
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public List<TypeParameterElem> getTypeParameters() {
            return super.getTypeParameters();
        }

        @Override
        public TypeMirror getSuperclass() {
            return super.getSuperclass();
        }

        @Override
        public void setSuperClass(TpeMirror superClass) {
            super.setSuperClass(superClass);
        }

        @Override
        public List<? extends TypeMirror> getInterfaces() {
            Optional<TypeElem> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                return solved.get().getInterfaces();
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public void setInterfaces(List<TpeMirror> interfaces) {
            Optional<TypeElem> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                solved.get().setInterfaces(interfaces);
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public <R, P> R accept(ElementVisitor<R, P> v, P p) {
            return super.accept(v, p);
        }

        @Override
        public TpeMirror asType() {
            return super.asType();
        }

        @Override
        public List<? extends AnnotationMirror> getAnnotationMirrors() {
            Optional<TypeElem> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                return solved.get().getAnnotationMirrors();
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return super.getAnnotation(annotationType);
        }

        @Override
        public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
            return super.getAnnotationsByType(annotationType);
        }

        @Override
        public Scope scope() {
            return super.scope();
        }

        @Override
        public String toString() {
            return super.toString();
        }
        
        private ClassRegistry classRegistry;
        private String name;
        private EnclosedRegistry enclosedRegistry;

        ProxyForClassToSolve(ClassRegistry classRegistry, EnclosedRegistry enclosedRegistry, String name){
            super(null, null, null, null, null, null, null, null);
            this.classRegistry = classRegistry;
            this.enclosedRegistry = enclosedRegistry;
            this.name = name;
        }
        
    }
    
    private class EnclosedRegistry {
        private Multimap<String, Elem> enclosedBuffer = HashMultimap.create();
        
        public void record(String name, Elem enclosed) {
            enclosedBuffer.put(name, enclosed);
        }
        
        public Collection<Elem> get(String name){
            return enclosedBuffer.get(name);
            
        }
    }
    
    private EnclosedRegistry enclosedRegistry = new EnclosedRegistry();
    
    private ClassRegistry classRegistry;
    
    public ModelBuilder(ClassRegistry classRegistry){
        this.classRegistry = classRegistry;
    }
    
    public TypeElem build(ClasspathElement aClass) throws IOException {
        ClassReader classReader = new ClassReader(aClass.getInputStream());
        CompiledClassOrigin origin = new CompiledClassOrigin();
        EltSimpleName simpleName = internalToSimple(classReader.getClassName());
        EltName absoluteName = internalToName(classReader.getClassName());
        
        TypeElem enclosing = null;
        NestingKind nestingKind = NestingKind.TOP_LEVEL;
        if (isInternal(classReader)) {
            nestingKind = NestingKind.MEMBER;
            int index = classReader.getClassName().lastIndexOf('$');
            assert index != -1;
            String enclosingName = classReader.getClassName().substring(0, index);
            enclosing = new ProxyForClassToSolve(classRegistry, enclosedRegistry, enclosingName);
        }

        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(aClass.getInputStream());
        
        ElementKind kind = ctClass.isInterface() ? ElementKind.INTERFACE : ElementKind.CLASS;
        TypeElem typeElem = new TypeElem(origin, null, enclosing, getModifiers(ctClass), absoluteName, simpleName, kind, nestingKind);

        for (Elem enclosed : enclosedRegistry.get(classReader.getClassName())){
            typeElem.addEnclosedElem(enclosed);
        }
        
        List<TpeMirror> interfaces = new ArrayList<TpeMirror>();
        for (String name : classReader.getInterfaces()) {
            // TODO implement
        }
        typeElem.setInterfaces(interfaces);
        return typeElem;
    }
    
    private Set<Modifier> getModifiers(CtClass ctClass) {
        Set<Modifier> modifiers = EnumSet.noneOf(Modifier.class);
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.ABSTRACT) > 0){
            modifiers.add(Modifier.ABSTRACT);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.FINAL) > 0){
            modifiers.add(Modifier.FINAL);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.NATIVE) > 0){
            modifiers.add(Modifier.NATIVE);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.PRIVATE) > 0){
            modifiers.add(Modifier.PRIVATE);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.PROTECTED) > 0){
            modifiers.add(Modifier.PROTECTED);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.PUBLIC) > 0){
            modifiers.add(Modifier.PUBLIC);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.STATIC) > 0){
            modifiers.add(Modifier.STATIC);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.STRICT) > 0){
            modifiers.add(Modifier.STRICTFP);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.SYNCHRONIZED) > 0){
            modifiers.add(Modifier.SYNCHRONIZED);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.TRANSIENT) > 0){
            modifiers.add(Modifier.TRANSIENT);
        }
        if ((ctClass.getModifiers() & java.lang.reflect.Modifier.VOLATILE) > 0){
            modifiers.add(Modifier.VOLATILE);
        }
        return modifiers;
    }
    
    private boolean isInternal(ClassReader reader) {
        return lastSegment(reader.getClassName()).contains("$");
    }
    
    private EltName internalToName(String internalName) {
        return EltNames.make(internalName);
    }

    private String lastSegment(String internalName) {
        String name = internalName;
        int index = internalName.lastIndexOf('/');
        if (index != -1){
            name = internalName.substring(index + 1);
        }
        return name;
    }
    
    private EltSimpleName internalToSimple(String internalName) {
        String name = lastSegment(internalName);
        int index = internalName.lastIndexOf('$');
        if (index != -1){
            name = internalName.substring(index + 1);
        }
        return EltNames.makeSimple(name);
    }
    
}
