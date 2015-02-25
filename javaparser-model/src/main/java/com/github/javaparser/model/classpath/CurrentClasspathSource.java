package com.github.javaparser.model.classpath;

import java.io.IOException;
import java.util.Set;

/**
 * This is a source fo ClasspathElement which considers all the current
 * entries of the Classpath as specified by the "java.class.path" property.
 *
 * @author Federico Tomassetti
 */
public class CurrentClasspathSource implements ClasspathSource {
    private final ResourceHelper resourceHelper;
    private final String prefix;

    /**
     * Limit the ClasspathElements considered to the ones having a path
     * starting with the given prefix.
     */
    public CurrentClasspathSource(String prefix) {
        this(ResourceHelper.classpathExcludingJre(), prefix);
    }

    public CurrentClasspathSource(ResourceHelper resourceHelper, String prefix) {
        this.resourceHelper = resourceHelper;
        this.prefix = prefix;
    }

    @Override
    public Set<ClasspathElement> getElements() throws IOException {
        return resourceHelper.listElements(prefix);
    }
}
