package co.com.automatizacion.runners;

import co.com.automatizacion.utils.datadriven.BeforeSuite;
import co.com.automatizacion.utils.datadriven.CustomCucumberWithSerenityRunner;
import co.com.automatizacion.utils.datadriven.csv.DynamicExamplesCsvPreProcessor;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(CustomCucumberWithSerenityRunner.class)
@CucumberOptions(
        features = "src/test/resources/features/user.feature",
        glue = {
                "co.com.automatizacion.hooks",
                "co.com.automatizacion.stepdefinitions"
        },
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class Runner {
        private Runner() {}

        @BeforeSuite
        public static void setUp() throws IOException {
                DynamicExamplesCsvPreProcessor.init();
        }
}
