package com.github.javaparser.model.classpath;

import java.io.IOException;
import java.io.InputStream;

/**
 * An element on the classpath.
 * 
 * It represents a single file on the classpath. It could be for example a class file or a file
 * part of the resources.
 *
 * @author Federico Tomassetti
 */
public abstract class ClasspathElement {
	private final String path;

	protected ClasspathElement(String path) {
		this.path = path;
	}

	/**
	 * The path of the element relative to the root of the classpath.
	 * For example a class file Foo in package a.b.c should have path a/b/c/Foo.class
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * The content of the element.
	 */
	public abstract InputStream getInputStream() throws IOException;
}
