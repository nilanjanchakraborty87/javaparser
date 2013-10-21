package japa.parser.ast.visitor;

import japa.parser.ast.Node;

public abstract class TreeVisitor {

    public final void visitDepthFirst(Node node){
        visitAction(node);
        for (Node child : node.getChildrenNodes()){
            visitDepthFirst(child);
        }
    }

    protected abstract void visitAction(Node node);
}
