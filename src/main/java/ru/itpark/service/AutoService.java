package ru.itpark.service;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static ru.itpark.query.AutoQuery.CREATE_TABLE_QUERY;
import static ru.itpark.query.AutoQuery.DELETE_QUERY;
import static ru.itpark.query.AutoQuery.INSERT_QUERY;
import static ru.itpark.query.AutoQuery.SELECT_QUERY;
import static ru.itpark.query.AutoQuery.UPDATE_QUERY;
import static ru.itpark.utils.DBUtils.addFilters;
import static ru.itpark.utils.DBUtils.fillPlaceholders;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ru.itpark.domain.Auto;
import ru.itpark.template.DBTemplate;

public class AutoService {

    private final DBTemplate db;

    public AutoService() throws NamingException, SQLException {
        var context = new InitialContext();
        db = (DBTemplate) context.lookup("java:/comp/env/bean/db-template");
        db.executeMethod(CREATE_TABLE_QUERY);
    }

    public String save(Auto auto) {
        Map<String, Object> map = auto.toMap();
        db.executeMethod(
                fillPlaceholders(isEmpty(auto.getId()) ? INSERT_QUERY : UPDATE_QUERY, map));
        return (String) map.get("id");
    }

    public void delete(String UID) {
        db.executeMethod(fillPlaceholders(DELETE_QUERY, Map.of("id", UID)));
    }

    public List<Auto> getAutoByParams(Map<String, Object> whereParams) {
        return db.executeQueryMethod(addFilters(SELECT_QUERY, whereParams));
    }


}
