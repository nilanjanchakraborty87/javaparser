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
    
    private List<TypeElem> classes = new ArrayList<TypeElem>();
    
    public void record(TypeElem elem) {
        classes.add(elem);
    }
    
    public int getCount() {
        return classes.size();
    }
    
    public Optional<TypeElem> getByName(String name){
        for (TypeElem element : classes) {
            if (element.getQualifiedName().contentEquals(name)){
                return Optional.of(element);
            }
        }
        return Optional.absent();
    }
    
}
