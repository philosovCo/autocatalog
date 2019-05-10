package ru.itpark.domain;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auto {

    private String id;
    private String name;
    private String description;
    private String picture;
    private String color;
    private String year;
    private String transmission;
    private String power;


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", StringUtils.isNoneBlank(id) ? id : UUID.randomUUID().toString());
        map.put("name", name);
        map.put("year", year);
        map.put("color", color);
        map.put("power", power);
        map.put("transmission", transmission);
        map.put("description", description);
        map.put("picture", picture);
        return map;
    }

    public Auto toModel(Map<String, Object> map) {
        id = (String) map.get("id");
        name = (String) map.get("name");
        year = (String) map.get("year");
        color = (String) map.get("color");
        power = (String) map.get("power");
        transmission = (String) map.get("transmission");
        description = (String) map.get("description");
        picture = (String) map.get("picture");
        return this;
    }


}
