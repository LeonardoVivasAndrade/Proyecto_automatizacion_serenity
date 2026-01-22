package co.com.automatizacion.utils.datadriven.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVReader {
    private static final Logger log = LoggerFactory.getLogger(CSVReader.class);
    private static final char DEFAULT_SEPARATOR = ',';

    private CSVReader() {
    }

    public static List<Map<String, String>> getData(String filePath) {
        List<Map<String, String>> rowsData = new ArrayList();
        Map<Integer, String> rowHeaderPosition = new HashMap();
        Map<String, String> headerRow = null;

        try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String lineData;
            while((lineData = bfReader.readLine()) != null) {
                List<String> line = parseLine(lineData);
                if (rowHeaderPosition.isEmpty()) {
                    rowHeaderPosition = generateHeaderPosition(line);
                    headerRow = generateRowData(line, rowHeaderPosition);
                } else {
                    Map<String, String> rowData = generateRowData(line, rowHeaderPosition);
                    rowsData.add(rowData);
                }
            }

            if (headerRow != null) {
                rowsData.add(headerRow);
            }
        } catch (Exception e) {
            log.error("ERROR en lectura de archivo CSV: " + e.getMessage(), e);
        }

        return rowsData;
    }

    private static Map<String, String> generateRowData(List<String> line, Map<Integer, String> rowHeaderPosition) {
        Map<String, String> result = new LinkedHashMap();

        for(int i = 0; i < line.size(); ++i) {
            String value = (String)line.get(i);
            result.put((String)rowHeaderPosition.getOrDefault(i, ""), value);
        }

        return result;
    }

    private static Map<Integer, String> generateHeaderPosition(List<String> line) {
        Map<Integer, String> result = new HashMap();

        for(int i = 0; i < line.size(); ++i) {
            result.put(i, (String)line.get(i));
        }

        return result;
    }

    public static List<String> parseLine(String csvLine) {
        return parseLine(csvLine, ',');
    }

    public static List<String> parseLine(String csvLine, char separators) {
        List<String> result = new ArrayList();
        if (csvLine.isEmpty()) {
            return result;
        } else {
            if (separators == ' ') {
                separators = ',';
            }

            StringBuilder separateWords = new StringBuilder();
            char[] chars = csvLine.toCharArray();

            for(char ch : chars) {
                if (ch == '\n') {
                    break;
                }

                if (ch != '\r') {
                    if (ch == separators) {
                        result.add(separateWords.toString());
                        separateWords.setLength(0);
                    } else {
                        separateWords.append(ch);
                    }
                }
            }

            result.add(separateWords.toString());
            return result;
        }
    }
}
