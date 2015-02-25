package com.github.javaparser.model.compiled;

import com.github.javaparser.model.classpath.ClasspathElement;
import com.github.javaparser.model.element.TypeElem;
import com.github.javaparser.model.scope.EltName;
import com.github.javaparser.model.scope.EltNames;
import com.github.javaparser.model.scope.EltSimpleName;
import org.objectweb.asm.ClassReader;

import javax.lang.model.element.TypeElement;
import java.io.IOException;

/**
 * @author Federico Tomassetti
 */
public class ModelBuilder {
    
    public TypeElement build(ClasspathElement aClass) throws IOException {
        ClassReader classReader = new ClassReader(aClass.getInputStream());
        CompiledClassOrigin origin = new CompiledClassOrigin();
        EltSimpleName simpleName = internalToSimple(classReader.getClassName());
        EltName absoluteName = internalToName(classReader.getClassName());
        TypeElem typeElem = new TypeElem(origin, null, null, null, absoluteName, simpleName, null, null);
        return typeElem;
    }
    
    private EltName internalToName(String internalName) {
        return EltNames.make(internalName);
    }

    private EltSimpleName internalToSimple(String internalName) {
        String name = internalName;
        int index = internalName.lastIndexOf('/');
        if (index != -1){
            name = internalName.substring(index + 1);
        }
        index = internalName.lastIndexOf('$');
        if (index != -1){
            name = internalName.substring(index + 1);
        }
        return EltNames.makeSimple(name);
    }
    
}
