package com.github.javaparser.model.classpath;

import java.io.IOException;
import java.util.Set;

/**
 * Represent a source of ClasspathElements.
 * 
 * It could correspond for example to a single Classpath entry (like a jar or a directory) or it could be an aggregation
 * of entries.
 *
 * @author Federico Tomassetti
 */
public interface ClasspathSource {

	/**
	 * Leaves of the trees.
	 */
	Set<ClasspathElement> getElements() throws IOException;
}
