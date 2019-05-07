package ru.itpark.utils;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Part;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class CSVUtils {

    private static final String PICTURE_COLUMN = "picture";
    private static final String HOST_URL = System.getenv("HOST_URL");

    public static final String DELIMITER = ",";
    public static final String DELIMITER_NEWLINE = "\n";
    public static final String IMAGES_URL = HOST_URL + "/images/";


    public static List<Map<String, Object>> read(Part part) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(part.getInputStream()))) {
            String[] row;
            String[] columns = null;
            while ((row = csvReader.readNext()) != null) {
                if (ArrayUtils.isEmpty(row) || Arrays.stream(row).allMatch(StringUtils::isEmpty)) {
                    continue;
                }

                Map<String, Object> map = new HashMap<>();
                if (columns == null) {
                    columns = row;
                    continue;
                }

                String[] fields =
                        columns.length == row.length ? row : Arrays.copyOf(row, columns.length);

                for (int i = 0; i < columns.length; i++) {
                    map.put(columns[i], fields[i]);
                }

                list.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String generateRow(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        String values = map.entrySet().stream()
                .map(entry ->
                        (PICTURE_COLUMN.equals(entry.getKey()) ? IMAGES_URL : EMPTY) +
                                entry.getValue())
                .collect(joining(DELIMITER))
                .replaceAll(DELIMITER_NEWLINE, EMPTY);
        stringBuilder.append(values);
        return stringBuilder.toString();
    }

    public static String generateHeader(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        String header = String.join(DELIMITER, map.keySet());
        stringBuilder.append(header).append(DELIMITER_NEWLINE);
        return stringBuilder.toString();
    }
}
