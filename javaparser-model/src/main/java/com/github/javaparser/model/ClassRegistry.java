package com.github.javaparser.model;

import com.github.javaparser.model.element.TypeElem;
import com.google.common.base.Optional;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Federico Tomassetti
 */
public class ClassRegistry {
    
    private List<TypeElement> classes = new ArrayList<TypeElement>();
    
    public void record(TypeElement elem) {
        classes.add(elem);
    }
    
    public int getCount() {
        return classes.size();
    }
    
    public Optional<TypeElement> getByName(String name){
        for (TypeElement element : classes) {
            if (element.getQualifiedName().contentEquals(name)){
                return Optional.of(element);
            }
        }
        return Optional.absent();
    }
    
}
