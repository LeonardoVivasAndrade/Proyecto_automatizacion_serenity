package co.com.automatizacion.utils.datadriven.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import co.com.automatizacion.utils.datadriven.PathConstants;
import net.thucydides.core.environment.SystemEnvironmentVariables;
import net.thucydides.core.util.EnvironmentVariables;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureOverwrite {
    private static final Logger log = LoggerFactory.getLogger(FeatureOverwrite.class);
    private static final EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static List<String> featuresList = new ArrayList();
    private static boolean externalDataProcessIsEmpty;

    private FeatureOverwrite() {
    }

    public static void overwriteFeatureFileAdd(String featurePath) throws IOException {
        addExternalDataToFeature(featurePath);
    }

    private static void addExternalDataToFeature(String featurePath) throws IOException {
        File featureFile = new File(featurePath);
        List<String> featureWithExternalData;
        if (featurePath.toLowerCase().contains("manual.feature")) {
            featureWithExternalData = impSetPaneOrCsvDataToFeature(featureFile);
        } else {
            featureWithExternalData = impSetFileDataToFeature(featureFile);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8)) {
            for(String writeLine : featureWithExternalData) {
                writer.write(writeLine);
                writer.write("\n");
            }
        }

    }

    private static void externalDataProcess(String data, List<String> fileData) {
        String filePath = getValidFilePath(data);
        String var10000 = PathConstants.dataPath();
        List<Map<String, String>> externalData = getDataFromFile(var10000 + PathConstants.validatePath(filePath));
        if (!externalData.isEmpty()) {
            externalDataProcessIsEmpty = false;
            Collection<String> headers = ((Map)externalData.get(0)).keySet();
            fileData.add(getGherkinExample(headers));

            for(int rowNumber = 0; rowNumber < externalData.size() - 1; ++rowNumber) {
                Collection<String> rowValues = ((Map)externalData.get(rowNumber)).values();
                String example = getGherkinExample(rowValues);
                if (!"".equals(example)) {
                    fileData.add(example);
                }
            }
        } else {
            externalDataProcessIsEmpty = true;
        }

    }

    private static List<String> impSetFileDataToFeature(File featureFile) throws IOException {
        List<String> fileData = new ArrayList();

        try (BufferedReader buffReader = Files.newBufferedReader(Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8)) {
            List<String> staticDataExample = new ArrayList();
            boolean exampleData = false;

            String data;
            while((data = buffReader.readLine()) != null) {
                if (data.trim().contains("@externaldata")) {
                    externalDataProcess(data, fileData);
                    exampleData = false;
                    data = validateExternaldataProcess(data);
                } else if ((data.trim().startsWith("@") || data.trim().startsWith("Scenario")) && exampleData) {
                    exampleData = false;
                    fileData.addAll(staticDataExample);
                } else {
                    log.debug("- No cumple condiciones -");
                }

                if (!exampleData) {
                    fileData.add(data);
                } else {
                    staticDataExample.add(data);
                }

                if (data.contains("Examples")) {
                    staticDataExample.clear();
                    exampleData = true;
                }
            }

            if (exampleData && !staticDataExample.isEmpty()) {
                fileData.addAll(staticDataExample);
                staticDataExample.clear();
            }
        }

        return fileData;
    }

    private static String validateExternaldataProcess(String data) {
        return !externalDataProcessIsEmpty && !data.trim().startsWith("#") ? "#" + data : data;
    }

    private static String getValidFilePath(String data) {
        return data.substring(StringUtils.ordinalIndexOf(data, "@", 2) + 1).replace("|", "").trim();
    }

    private static List<Map<String, String>> getDataFromFile(String filePath) {
        return isCSV(filePath) ? CSVReader.getData(filePath) : null;
    }

    private static boolean isCSV(String filePath) {
        return filePath.toLowerCase().trim().endsWith(".csv");
    }

    private static String getGherkinExample(Collection<String> examplesFields) {
        String example = "";

        for(String field : examplesFields) {
            example = String.format("%s|%s", example, field);
        }

        return "".equals(example) ? "" : example + "|";
    }

    private static List<String> impSetPaneOrCsvDataToFeature(File featureFile) throws IOException {
        List<String> fileData = new ArrayList();

        try (
                BufferedReader buffReader = Files.newBufferedReader(Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8);
                BufferedReader buffReaderScenario = Files.newBufferedReader(Paths.get(featureFile.getAbsolutePath()), StandardCharsets.UTF_8);
        ) {
            List<String> scenarios = new ArrayList();
            int numScenario = 0;
            String azureOrLocalExecution = variables.getProperty("execute");

            String nameScenario;
            while((nameScenario = buffReaderScenario.readLine()) != null) {
                if (nameScenario.trim().contains("Scenario:")) {
                    scenarios.add(nameScenario);
                }
            }
        }

        return fileData;
    }

    public static List<String> listFilesByFolder(String featureName, File folder) {
        String ALL_FEATURES = "todos";
        if (featureName.equalsIgnoreCase("todos")) {
            for(File fileOrFolder : (File[])Objects.requireNonNull(folder.listFiles())) {
                if (fileOrFolder.isDirectory()) {
                    listFilesByFolder(featureName, fileOrFolder);
                } else {
                    featuresList.add(fileOrFolder.getAbsolutePath());
                }
            }
        } else {
            featuresList = List.of(featureName.split(";"));
        }

        return new ArrayList(featuresList);
    }

    public static void clearListFilesByFolder() {
        featuresList.clear();
    }
}

