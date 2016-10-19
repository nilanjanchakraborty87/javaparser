package com.github.javaparser.ast;

import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A node that is a list of nodes.
 *
 * @param <N> the type of nodes contained.
 */
public class NodeList<N extends NodeContainer> implements Iterable<N>, NodeContainer {
    private NodeContainer parentContainer;

    // TODO we probably want to use the already existing childrenNodes list for this.
    private List<N> innerList = new ArrayList<>(0);

    public NodeList() {
        this(null);
    }

    public NodeList(Node container) {
        setParentNode(container);
    }

    public NodeList<N> add(N node) {
        own(node);
        innerList.add(node);
        return this;
    }

    @Override
    public void addChild(NodeContainer node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeChild(NodeContainer node) {
        throw new UnsupportedOperationException();
    }

    private void own(N node) {
        if (node == null) {
            return;
        }
        setAsParentNodeOf(node);
    }

    public boolean remove(Node node) {
        boolean remove = innerList.remove(node);
        node.setParentNode(null);
        return remove;
    }

    public static <X extends Node> NodeList<X> nodeList(X... nodes) {
        final NodeList<X> nodeList = new NodeList<>();
        for (X node : nodes) {
            nodeList.add(node);
        }
        return nodeList;
    }

    public static <X extends Node> NodeList<X> nodeList(Collection<X> nodes) {
        final NodeList<X> nodeList = new NodeList<>();
        for (X node : nodes) {
            nodeList.add(node);
        }
        return nodeList;
    }

    public static <X extends Node> NodeList<X> nodeList(NodeList<X> nodes) {
        final NodeList<X> nodeList = new NodeList<>();
        for (X node : nodes) {
            nodeList.add(node);
        }
        return nodeList;
    }

    public boolean contains(N node) {
        return innerList.contains(node);
    }

    public Stream<N> stream() {
        return innerList.stream();
    }

    public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
        return v.visit(this, arg);
    }

    public <A> void accept(final VoidVisitor<A> v, final A arg) {
        v.visit(this, arg);
    }

    public int size() {
        return innerList.size();
    }

    public N get(int i) {
        return innerList.get(i);
    }

    @Override
    public Iterator<N> iterator() {
        // TODO take care of "Iterator.remove"
        return innerList.iterator();
    }

    public NodeList<N> set(int index, N element) {
        setAsParentNodeOf(element);
        innerList.set(index, element);
        return this;
    }

    public NodeList<N> remove(int index) {
        innerList.remove(index);
        return this;
    }

    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    public void ifNotEmpty(Consumer<NodeList<N>> consumer) {
        if (!isEmpty()) {
            consumer.accept(this);
        }
    }

    public void sort(Comparator<? super N> comparator) {
        Collections.sort(innerList, comparator);
    }

    public void addAll(NodeList<N> otherList) {
        for (N node : otherList) {
            add(node);
        }
    }

    public NodeList<N> add(int index, N node) {
        own(node);
        innerList.add(index, node);
        return this;
    }

    @Override
    public void setParentNode(NodeContainer node) {
        parentContainer = node;
    }

    @Override
    public void setAsParentNodeOf(NodeContainer child) {
        if (child != null) {
            child.setParentNode(this);
        }
    }

    @Override
    public NodeContainer getParentNode() {
        return parentContainer;
    }
}
