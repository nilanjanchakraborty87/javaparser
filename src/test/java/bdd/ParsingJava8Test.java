package bdd;

import bdd.embedders.ManipulationEmbedder;
import bdd.embedders.ParsingEmbedder;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.StoryFinder;
import org.junit.Test;

import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

public class ParsingJava8Test {

    @Test
    public void run() throws Throwable {
        Embedder embedder = new ManipulationEmbedder();
        List<String> storyPaths = new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/bdd/parsing_java8_scenarios.story", "");
        embedder.runStoriesAsPaths(storyPaths);
    }
}
