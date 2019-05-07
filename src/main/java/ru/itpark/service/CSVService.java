package ru.itpark.service;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static ru.itpark.utils.CSVUtils.DELIMITER_NEWLINE;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Part;
import ru.itpark.domain.Auto;
import ru.itpark.utils.CSVUtils;

public class CSVService {

    private AutoService service;
    private ImageService imageService;

    public CSVService() throws NamingException {
        var context = new InitialContext();
        service = (AutoService) context.lookup("java:/comp/env/bean/auto-service");
        imageService = (ImageService) context.lookup("java:/comp/env/bean/image-service");
    }

    public void generateFile(OutputStream outputStream, Map<String, Object> param)
            throws IOException {
        List<Auto> autoList = service.getAutoByParams(param);
        if (isEmpty(autoList)) {
            throw new RuntimeException("Empty List");
        }
        String header = CSVUtils.generateHeader(autoList.stream().findFirst().get().toMap());

        String rows = autoList.stream()
                .map(Auto::toMap)
                .map(CSVUtils::generateRow)
                .collect(Collectors.joining(DELIMITER_NEWLINE));

        String result = header + rows;
        outputStream.write(result.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public void saveByCsv(Part part) throws IOException {
        CSVUtils.read(part).forEach(
                map -> service
                        .save(new Auto().toModel(map.get("id") == null ? map : updateModel(map)))
        );
    }

    public Map<String, Object> updateModel(Map<String, Object> map) {
        List<Auto> autoList = service.getAutoByParams(Map.of("id", map.get("id")));
        map.replace("picture", imageService.getImageByUrl((String) map.get("picture")));
        if (isEmpty(autoList)) {
            map.remove("id");
            return map;
        }

        Map<String, Object> autoMap = new HashMap<>();
        autoList.get(0).toMap().forEach((s, o) -> {
            String value = (String) map.get(s);
            autoMap.put(s, (value != null ? value : o));
        });

        return autoMap;
    }
}
