package com.github.javaparser.model;

import com.github.javaparser.model.classpath.CurrentClasspathSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Federico Tomassetti
 */
@RunWith(JUnit4.class)
public class CompiledClassBasicMethodsTest {
    
    private static class MyJavaFileManager implements JavaFileManager {
        @Override
        public int isSupportedOption(String option) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return null;
        }

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
            System.out.println("L "+location+" "+packageName);
            //throw new UnsupportedOperationException();
            return Collections.<JavaFileObject>emptyList();
        }

        @Override
        public String inferBinaryName(Location location, JavaFileObject file) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isSameFile(FileObject a, FileObject b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean handleOption(String current, Iterator<String> remaining) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasLocation(Location location) {
            if (location.getName().equals("ANNOTATION_PROCESSOR_PATH")) return false;
            System.out.println("Has location " + location.getName());
            if (location.getName().equals("SOURCE_PATH")) return false;
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flush() throws IOException {
            // nothing to do
        }

        @Override
        public void close() throws IOException {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class CompileSourceInMemory {
        public static void compile(String name, String code) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

            JavaFileObject file = new JavaSourceFromString(name, code);

            Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
            MyJavaFileManager javaFileManager = new MyJavaFileManager();
            JavaCompiler.CompilationTask task = compiler.getTask(null, javaFileManager, diagnostics, null, null, compilationUnits);

            boolean success = task.call();
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                System.out.println(diagnostic.getCode());
                System.out.println(diagnostic.getKind());
                System.out.println(diagnostic.getPosition());
                System.out.println(diagnostic.getStartPosition());
                System.out.println(diagnostic.getEndPosition());
                System.out.println(diagnostic.getSource());
                System.out.println(diagnostic.getMessage(null));

            }
            System.out.println("Success: " + success);

            if (success) {
                try {
                    Class.forName("HelloWorld").getDeclaredMethod("main", new Class[] { String[].class })
                            .invoke(null, new Object[] { null });
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found: " + e);
                } catch (NoSuchMethodException e) {
                    System.err.println("No such method: " + e);
                } catch (IllegalAccessException e) {
                    System.err.println("Illegal access: " + e);
                } catch (InvocationTargetException e) {
                    System.err.println("Invocation target: " + e);
                }
            }
        }
    }

    static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
    
    @Test
    public void foo() throws IOException {
        CompileSourceInMemory.compile("A", "class A{}");
    } 
}
