package com.github.javaparser.model.compiled;

import com.github.javaparser.model.ClassRegistry;
import com.github.javaparser.model.classpath.ClasspathElement;
import com.github.javaparser.model.element.Elem;
import com.github.javaparser.model.element.TypeElem;
import com.github.javaparser.model.scope.EltName;
import com.github.javaparser.model.scope.EltNames;
import com.github.javaparser.model.scope.EltSimpleName;
import com.google.common.base.Optional;
import org.objectweb.asm.ClassReader;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * @author Federico Tomassetti
 */
public class ModelBuilder {
    
    private class ProxyForClassToSolve implements TypeElement {

        @Override
        public List<? extends Element> getEnclosedElements() {
            return null;
        }

        @Override
        public NestingKind getNestingKind() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Name getQualifiedName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Name getSimpleName() {
            Optional<TypeElement> solved = classRegistry.getByName(name);
            if (solved.isPresent()) {
                return solved.get().getSimpleName();
            } else {
                throw new RuntimeException("Unsolved reference to class "+name);
            }
        }

        @Override
        public TypeMirror getSuperclass() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<? extends TypeMirror> getInterfaces() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<? extends TypeParameterElement> getTypeParameters() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Element getEnclosingElement() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TypeMirror asType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ElementKind getKind() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<Modifier> getModifiers() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<? extends AnnotationMirror> getAnnotationMirrors() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <R, P> R accept(ElementVisitor<R, P> v, P p) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
            throw new UnsupportedOperationException();
        }
        
        private ClassRegistry classRegistry;
        private String name;

        ProxyForClassToSolve(ClassRegistry classRegistry, String name){
            this.classRegistry = classRegistry;
            this.name = name;
        }
        
    }
    
    private ClassRegistry classRegistry;
    
    public ModelBuilder(ClassRegistry classRegistry){
        this.classRegistry = classRegistry;
    }
    
    public TypeElement build(ClasspathElement aClass) throws IOException {
        ClassReader classReader = new ClassReader(aClass.getInputStream());
        CompiledClassOrigin origin = new CompiledClassOrigin();
        EltSimpleName simpleName = internalToSimple(classReader.getClassName());
        EltName absoluteName = internalToName(classReader.getClassName());
        
        Element enclosing = null;
        if (isInternal(classReader)) {
            int index = classReader.getClassName().lastIndexOf('$');
            assert index != -1;
            String enclosingName = classReader.getClassName().substring(0, index);
            enclosing = new ProxyForClassToSolve(classRegistry, enclosingName);
        }
        
            TypeElem typeElem = new TypeElem(origin, null, enclosing, null, absoluteName, simpleName, null, null);
        return typeElem;
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
