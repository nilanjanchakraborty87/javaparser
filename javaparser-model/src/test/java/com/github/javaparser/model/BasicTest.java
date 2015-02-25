package com.github.javaparser.model;

import com.github.javaparser.ParseException;
import com.github.javaparser.model.classpath.CurrentClasspathSource;
import com.github.javaparser.model.element.TypeElem;
import com.github.javaparser.model.report.DumpReporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Federico Tomassetti
 */
@RunWith(JUnit4.class)
public class BasicTest {

	@Test
	public void initialTest() throws IOException, ParseException {
		JavaAnalyser analyser = new JavaAnalyser(
				new AnalysisConfiguration()
						.reporter(new DumpReporter(new PrintWriter(System.out)))
		);

		Analysis model = analyser.buildModel(new CurrentClasspathSource("scenario_a"));

		assertEquals(1, model.getCompilationUnits().size());
		assertEquals(1, model.getSourcePackages().size());
		assertTrue(model.getSourcePackages().get(0).isUnnamed());

		assertEquals(1, model.getSourcePackages().get(0).getEnclosedElements().size());
		Element elmtClassA = model.getSourcePackages().get(0).getEnclosedElements().get(0);
		assertTrue(elmtClassA instanceof TypeElem);
		TypeElem typeClassA = (TypeElem) elmtClassA;
		assertTrue(elmtClassA.getSimpleName().contentEquals("A"));
	}


}
