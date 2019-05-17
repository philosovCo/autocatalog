package ru.itpark.service;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static ru.itpark.query.AutoQuery.CREATE_TABLE_QUERY;
import static ru.itpark.query.AutoQuery.DELETE_QUERY;
import static ru.itpark.query.AutoQuery.INSERT_QUERY;
import static ru.itpark.query.AutoQuery.SELECT_QUERY;
import static ru.itpark.query.AutoQuery.UPDATE_QUERY;
import static ru.itpark.utils.DBUtils.addFilters;
import static ru.itpark.utils.DBUtils.fillPlaceholders;

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

public class AutoService {

    private final DataSource ds;

    public AutoService() throws NamingException {
        var context = new InitialContext();
        ds = (DataSource) context.lookup("java:/comp/env/jdbc/db");
        executeMethod(CREATE_TABLE_QUERY);
    }

    public String save(Auto auto) {
        Map<String, Object> map = auto.toMap();
        executeMethod(
                fillPlaceholders(isEmpty(auto.getId()) ? INSERT_QUERY : UPDATE_QUERY, map));
        return (String) map.get("id");
    }

    public void delete(String UID) {
        executeMethod(fillPlaceholders(DELETE_QUERY, Map.of("id", UID)));
    }

    public List<Auto> getAutoByParams(Map<String, Object> whereParams) {
        return executeQueryMethod(addFilters(SELECT_QUERY, whereParams));
    }

    private void executeMethod(String q) {
        try (var conn = ds.getConnection();
                var stmt = conn.prepareStatement(q)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e); // rethrowing
        }
    }

    private List<Auto> executeQueryMethod(String q) {
        var list = new ArrayList<Auto>();

        try (var conn = ds.getConnection();
                var stmt = conn.prepareStatement(q);
                var rs = stmt.executeQuery()) {

            while (rs.next()) {
                var row = new HashMap<String, Object>();
                var md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }

                list.add(Auto.from(row));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e); // rethrowing
        }
        return list;
    }
}
