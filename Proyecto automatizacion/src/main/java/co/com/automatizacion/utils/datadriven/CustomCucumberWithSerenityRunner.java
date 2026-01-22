package co.com.automatizacion.utils.datadriven;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import co.com.automatizacion.exceptions.ExcRuntime;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCucumberWithSerenityRunner extends Runner {
    private static final Logger log = LoggerFactory.getLogger(CustomCucumberWithSerenityRunner.class);
    private final Class<?> testClass;
    private CucumberWithSerenity cucumberWithSerenity;

    public CustomCucumberWithSerenityRunner(Class<?> testClass) {
        this.testClass = testClass;

        try {
            this.cucumberWithSerenity = new CucumberWithSerenity(testClass);
        } catch (InitializationError e) {
            throw new ExcRuntime("Error inicializando CucumberWithSerenity", e);
        }
    }

    public Description getDescription() {
        return this.cucumberWithSerenity.getDescription();
    }

    private void runAnnotatedMethods(Class<? extends Annotation> annotation) {
        for(Method method : this.testClass.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                try {
                    method.invoke((Object)null);
                } catch (Exception e) {
                    log.error("Error ejecutando método anotado: {}", method.getName(), e);
                    throw new ExcRuntime("Error ejecutando método anotado", e);
                }
            }
        }

    }

    public void run(RunNotifier notifier) {
        try {
            this.runAnnotatedMethods(BeforeSuite.class);
            this.cucumberWithSerenity = new CucumberWithSerenity(this.testClass);
        } catch (Exception e) {
            log.error("Error al ejecutar métodos @BeforeSuite: {}", e.getMessage(), e);
        }

        this.cucumberWithSerenity.run(notifier);
    }
}

