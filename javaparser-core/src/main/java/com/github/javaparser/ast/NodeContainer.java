package com.github.javaparser.ast;

import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

/**
 * It can contain nodes without being necessarily a Node.
 */
public interface NodeContainer {
    void setParentNode(Node node);
    void setAsParentNodeOf(NodeContainer node);

    /**
     * Accept method for visitor support.
     *
     * @param <R>
     *            the type the return value of the visitor
     * @param <A>
     *            the type the argument passed to the visitor
     * @param v
     *            the visitor implementation
     * @param arg
     *            the argument passed to the visitor
     * @return the result of the visit
     */
    <R, A> R accept(GenericVisitor<R, A> v, A arg);

    /**
     * Accept method for visitor support.
     *
     * @param <A>
     *            the type the argument passed for the visitor
     * @param v
     *            the visitor implementation
     * @param arg
     *            any value relevant for the visitor
     */
    <A> void accept(VoidVisitor<A> v, A arg);

    NodeContainer getParentNode();
}
