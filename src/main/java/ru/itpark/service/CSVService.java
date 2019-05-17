package ru.itpark.service;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Part;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ru.itpark.domain.Auto;

public class CSVService {

    private static final String PICTURE_COLUMN = "picture";
    private static final String HOST_URL = System.getenv("HOST_URL");
    public static final String IMAGES_URL = HOST_URL + "/images/";

    private AutoService service;
    private ImageService imageService;

    public CSVService() throws NamingException {
        var context = new InitialContext();
        service = (AutoService) context.lookup("java:/comp/env/bean/auto-service");
        imageService = (ImageService) context.lookup("java:/comp/env/bean/image-service");
    }

    public void generateFile(OutputStream outputStream, Map<String, Object> param) {
        List<Auto> autoList = service.getAutoByParams(param);
        if (isEmpty(autoList)) {
            throw new RuntimeException("Empty List");
        }

        Map<String, Object> map = autoList.stream().findFirst().get().toMap();
        Object[] keyArray = map.keySet().toArray();
        String[] rows = Arrays.copyOf(keyArray, keyArray.length, String[].class);

        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(rows);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            autoList.stream()
                    .map(Auto::toMap)
                    .peek(m -> m.replace(PICTURE_COLUMN, IMAGES_URL + m.get(PICTURE_COLUMN)))
                    .map(Map::values)
                    .map(Collection::toArray)
                    .forEach(objects -> {
                        try {
                            csvPrinter.printRecord(objects);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveByCsv(Part part) {

        try (Reader reader = new BufferedReader(
                new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8))) {

            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {
                Map<String, Object> map = csvRecord.toMap()
                        .entrySet()
                        .stream()
                        .peek(entry -> {
                                    if (PICTURE_COLUMN.equals(entry.getKey())) {
                                        entry.setValue(ImageService.DEFAULT_IMAGE_NAME);
                                    }
                                }
                        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                service.save(Auto.from(map.get("id") == null ? map : updateModel(map)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> updateModel(Map<String, Object> map) {
        Map<String, Object> autoMap = new HashMap<>();
        List<Auto> autoList = service.getAutoByParams(Map.of("id", map.get("id")));
        map.replace("picture", imageService.getImageByUrl((String) map.get("picture")));

        if (isEmpty(autoList)) {
            map.entrySet()
                    .stream()
                    .filter(e -> !"id".equals(e.getKey()))
                    .forEach(e -> autoMap.put(e.getKey(), e.getValue()));
        } else {
            autoList.get(0).toMap().forEach((s, o) -> {
                String value = (String) map.get(s);
                autoMap.put(s, (value != null ? value : o));
            });
        }
        return autoMap;
    }
}
