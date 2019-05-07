package ru.itpark.template;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import ru.itpark.domain.Auto;

public class DBTemplate {

    private final DataSource ds;

    public DBTemplate() throws NamingException {
        var context = new InitialContext();
        ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");
    }

    public void executeMethod(String q) {
        try (var conn = ds.getConnection();
                var stmt = conn.prepareStatement(q)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e); // rethrowing
        }
    }

    public List<Auto> executeQueryMethod(String q) {
        var list = new ArrayList<Auto>();
        try (var conn = ds.getConnection();
                var stmt = conn.prepareStatement(q);
                var rs = stmt.executeQuery()) {
            while (rs.next()) {
                Auto auto = new Auto();
                auto.toModel((resultSetToMap(rs)));
                list.add(auto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e); // rethrowing
        }
        return list;
    }

    private Map<String, Object> resultSetToMap(ResultSet rs) {
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(Auto.class.getDeclaredFields())
                .map(Field::getName)
                .forEach(field -> {
                    try {
                        map.put(field, rs.getString(field));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
        return map;
    }
}
