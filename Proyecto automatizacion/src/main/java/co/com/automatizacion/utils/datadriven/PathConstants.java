package co.com.automatizacion.utils.datadriven;

import java.io.File;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;

public class PathConstants {
    private static final EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String RESOURCES_PATH;
    private static final String FEATURE_PATH;
    private static final String DATA_PATH;

    private PathConstants() {
    }

    public static String resourcesPath() {
        return variables.getProperty("resources.path") != null ? variables.getProperty("resources.path") : RESOURCES_PATH;
    }

    public static String featurePath() {
        return variables.getProperty("features.path") != null ? variables.getProperty("features.path") : FEATURE_PATH;
    }

    public static String dataPath() {
        return variables.getProperty("data.path") != null ? variables.getProperty("data.path") : DATA_PATH;
    }

    public static String validatePath(String path) {
        String[] separators = new String[]{"/", "\\", "-"};

        for(String separator : separators) {
            if (path.contains(separator)) {
                path = path.replace(separator, File.separator);
                break;
            }
        }

        return path;
    }

    static {
        String var10000 = System.getProperty("user.dir");
        RESOURCES_PATH = var10000 + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;
        FEATURE_PATH = RESOURCES_PATH + "features" + File.separator;
        DATA_PATH = RESOURCES_PATH + "data" + File.separator;
    }
}
