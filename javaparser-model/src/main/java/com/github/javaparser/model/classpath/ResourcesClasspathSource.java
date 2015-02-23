package com.github.javaparser.model.classpath;

import java.io.IOException;
import java.security.interfaces.RSAKey;
import java.util.Set;

/**
 * @author Federico Tomassetti
 */
public class ResourcesClasspathSource implements ClasspathSource {
    private final ResourceHelper resourceHelper;
    private final String prefix;
    
    public ResourcesClasspathSource(String prefix) {
        this(ResourceHelper.classpathExcludingJre(), prefix);
    }

    public ResourcesClasspathSource(ResourceHelper resourceHelper, String prefix) {
        this.resourceHelper = resourceHelper;
        this.prefix = prefix;
    }

    @Override
    public Set<ClasspathElement> getElements() throws IOException {
        return resourceHelper.listElements(prefix);
    }
}
