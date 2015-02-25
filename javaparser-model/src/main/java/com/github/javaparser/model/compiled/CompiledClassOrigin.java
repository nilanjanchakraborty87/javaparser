package com.github.javaparser.model.compiled;

import com.github.javaparser.model.element.Origin;

/**
 * @author Federico Tomassetti
 */
public class CompiledClassOrigin implements Origin {
    
    @Override
    public String toLocationString() {
        throw new UnsupportedOperationException();
    }
}
