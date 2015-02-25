package com.github.javaparser.model.classpath;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Represent a classpath source which corresponds to a directory.
 *
 * @author Federico Tomassetti
 */
public class DirClasspathSource implements ClasspathSource {
    private File directory;
    private String basePath;

    /**
     * Directory considered of the classpath.
     * <p/>
     * All the elements contained will have a path relative to this directory.
     */
    public DirClasspathSource(File directory) {
        this(directory, "");
        if (!directory.isDirectory() || !directory.exists()) {
            throw new IllegalArgumentException("An existing directory is expected");
        }
    }

    /**
     * We explore the tree recursively, creating an instance for each subdirectory.
     * <p/>
     * The basePath specify the path of the directory represented by this, w.r.t. to the root of this ClasspathSource.
     */
    private DirClasspathSource(File directory, String basePath) {
        if (!directory.exists()) {
            throw new IllegalArgumentException("No such directory: " + directory.getAbsolutePath());
        }
        this.directory = directory;
        this.basePath = basePath;
    }

    /**
     * The subdirectories.
     */
    private Set<ClasspathSource> getSubtreesInternal() {
        Set<ClasspathSource> subtrees = new HashSet<ClasspathSource>();
        for (File child : directory.listFiles()) {
            String path = basePath.isEmpty() ? child.getName() : basePath + "/" + child.getName();
            if (child.isDirectory()) {
                subtrees.add(new DirClasspathSource(child, path));
            }
        }
        return subtrees;
    }

    @Override
    public Set<ClasspathElement> getElements() throws IOException {
        Set<ClasspathElement> sourceFiles = new HashSet<ClasspathElement>();

        sourceFiles.addAll(getElementsInternal());
        for (ClasspathSource subtree : getSubtreesInternal()) {
            sourceFiles.addAll(subtree.getElements());
        }
        return sourceFiles;
    }

    /**
     * The actual files contained in the current directory.
     */
    private Set<ClasspathElement> getElementsInternal() {
        Set<ClasspathElement> elements = new HashSet<ClasspathElement>();
        for (File child : directory.listFiles()) {
            String path = basePath.isEmpty() ? child.getName() : basePath + "/" + child.getName();
            if (child.isFile()) {
                elements.add(new FileClasspathElement(path, child));
            }
        }
        return elements;
    }
}
