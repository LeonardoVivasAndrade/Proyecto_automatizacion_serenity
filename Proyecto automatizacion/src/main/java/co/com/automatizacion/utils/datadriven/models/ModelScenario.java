package co.com.automatizacion.utils.datadriven.models;

import io.cucumber.java.Scenario;

public class ModelScenario {
    private static Scenario scenario;

    private ModelScenario() {
        throw new IllegalStateException("Utility class");
    }

    public static void setScenario(Scenario scenario) {
        scenario = scenario;
    }

    public static Scenario getScenario() {
        return scenario;
    }
}
