package com.github.javaparser.model;

import com.github.javaparser.model.classpath.CurrentClasspathSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.lang.model.element.TypeElement;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Federico Tomassetti
 */
@RunWith(JUnit4.class)
public class CompiledClassBasicTest {
    
    @Test
    public void foo() throws IOException {
        JavaAnalyser analyser = new JavaAnalyser();
        ClassRegistry classRegistry = analyser.buildModelForClasses(new CurrentClasspathSource("compiledclasses"));
        
        // We expect to find the two classes (one of which internal) with these given names
        assertEquals(2, classRegistry.getCount());
        assertTrue(classRegistry.getByName("com/github/javaparser/model/A").isPresent());
        assertTrue(classRegistry.getByName("com/github/javaparser/model/A$Test").isPresent());
        
        // class A
        TypeElement classA = classRegistry.getByName("com/github/javaparser/model/A").get();
        assertNull(classA.getEnclosingElement());
        assertEquals(1, classA.getEnclosedElements().size());

        // class A.Test
        TypeElement classTest = classRegistry.getByName("com/github/javaparser/model/A$Test").get();
        assertTrue(classTest.getEnclosingElement().getSimpleName().contentEquals("A"));
        assertEquals(0, classTest.getEnclosedElements().size());
    } 
}
