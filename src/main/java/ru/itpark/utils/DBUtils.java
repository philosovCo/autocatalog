package ru.itpark.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.upperCase;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;

public class DBUtils {

    public static String fillPlaceholders(String query, Map<String, Object> data) {
        String result = query;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            result = replace(result, ":" + entry.getKey(), setString(entry.getValue()));
        }
        return result;
    }

    public static String addFilters(String query, Map<String, Object> whereParams) {
        StringBuilder wheres = new StringBuilder();
        if (MapUtils.isNotEmpty(whereParams)) {
            whereParams.forEach((s, o) -> {
                        wheres.append(!isEmpty(wheres) ? " AND " : " WHERE ");
                        if ("name".equalsIgnoreCase(s)) {
                            wheres.append(" upper(").append(s).append(") like '%")
                                    .append(upperCase((String) o).trim()).append("%'");
                        } else {
                            wheres.append(s).append(" = '").append(o).append("'");
                        }
                    }
            );
        }
        return query + wheres.toString();
    }

    public static Map<String, Object> toMapWithSingleValue(Map<String, String[]> stringMap) {
        Map<String, Object> newMap = new HashMap<>();
        stringMap.forEach((s, strings) -> {
            if ((strings != null) && (strings.length == 1) && !isEmpty(strings[0])) {
                newMap.put(s, strings[0]);
            }
        });
        return newMap;
    }

    private static String setString(Object o) {
        return o != null ? "'" + o.toString() + "'" : null;
    }

}
