package com.github.javaparser.model;

import com.github.javaparser.model.classpath.Classpath;
import com.github.javaparser.model.classpath.ClasspathElement;
import com.github.javaparser.model.classpath.ClasspathSource;
import com.github.javaparser.model.classpath.DirClasspathSource;
import com.github.javaparser.model.compiled.ModelBuilder;
import com.github.javaparser.model.element.ElementUtils;
import com.github.javaparser.model.phases.Scaffolding;
import com.github.javaparser.model.phases.SurfaceTyping1;
import com.github.javaparser.model.phases.SurfaceTyping2;
import com.github.javaparser.model.phases.TypeResolver;
import com.github.javaparser.model.report.Reporter;
import com.github.javaparser.model.type.TypeUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Didier Villevalois
 */
public class JavaAnalyser {

	private final AnalysisConfiguration configuration;

	public JavaAnalyser() {
		this(new AnalysisConfiguration());
	}

	public JavaAnalyser(AnalysisConfiguration configuration) {
		this.configuration = configuration;
	}

	public Analysis buildModelForSources(final File sourceDirectory) throws IOException {
		return buildModelForSources(new DirClasspathSource(sourceDirectory));
	}

	public ClassRegistry buildModelForClasses(final File classDirectory) throws IOException {
		return buildModelForClasses(new DirClasspathSource(classDirectory));
	}

	public Analysis buildModelForSources(final ClasspathSource sources) throws IOException {
		Registry registry = new Registry();

		Classpath classpath = new Classpath();

		registry.register(classpath);
		registry.register(Reporter.class, configuration.getReporter());
		registry.register(new Scaffolding());
		registry.register(new TypeResolver());
		registry.register(new SurfaceTyping1());
		registry.register(new SurfaceTyping2());
		registry.register(new TypeUtils());
		registry.register(new ElementUtils());
		registry.configure();

		classpath.addSources(sources);

		Analysis analysis = new Analysis(configuration, registry);
		analysis.proceed();
		return analysis;
	}

	public ClassRegistry buildModelForClasses(final ClasspathSource classFiles) throws IOException {
		ClassRegistry classRegistry = new ClassRegistry();
		ModelBuilder modelBuilder = new ModelBuilder(classRegistry);
		for (ClasspathElement aClass : classFiles.getElements()) {
			classRegistry.record(modelBuilder.build(aClass));
		}

		return classRegistry;
	}
}
