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

    public static Auto from(Map<String, Object> map) {
        Auto auto = new Auto();
        auto.setId((String) map.get("id"));
        auto.setName((String) map.get("name"));
        auto.setYear((String) map.get("year"));
        auto.setColor((String) map.get("color"));
        auto.setPower((String) map.get("power"));
        auto.setTransmission((String) map.get("transmission"));
        auto.setDescription((String) map.get("description"));
        auto.setPicture((String) map.get("picture"));
        return auto;
    }


}
