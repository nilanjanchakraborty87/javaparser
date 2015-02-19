package com.github.javaparser.ast;

import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class Identifier extends Node {
    
    private final String label;
    
    public Identifier(int beginLine, int beginColumn, int endLine, int endColumn, String label) {
        super(beginLine, beginColumn, endLine, endColumn);
        this.label = label;
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    public String getLabel() {
        return label;
    }
}
