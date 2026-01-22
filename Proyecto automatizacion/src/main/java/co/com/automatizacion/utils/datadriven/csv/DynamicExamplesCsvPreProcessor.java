package co.com.automatizacion.utils.datadriven.csv;

import java.io.File;
import java.io.IOException;

import co.com.automatizacion.utils.datadriven.PathConstants;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;

public class DynamicExamplesCsvPreProcessor {
    private static final EnvironmentVariables VARIABLES = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String EXTENSION_FEATURE = ".feature";

    public DynamicExamplesCsvPreProcessor() {
    }

    public static void init() throws IOException {
        String featureName = "todos";
        if (featureName != null && !featureName.isBlank()) {
            for(String feature : FeatureOverwrite.listFilesByFolder(featureName, new File(PathConstants.featurePath()))) {
                if (feature.endsWith(".feature")) {
                    FeatureOverwrite.overwriteFeatureFileAdd(feature);
                }
            }

            FeatureOverwrite.clearListFilesByFolder();
        } else {
            throw new IllegalArgumentException("El nombre del feature no puede ser nulo o vacío");
        }
    }
}
