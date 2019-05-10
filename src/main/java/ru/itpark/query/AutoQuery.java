package ru.itpark.query;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import ru.itpark.domain.Auto;

public class AutoQuery {

    static {
        Supplier<Stream<String>> fieldsStream = () -> Arrays.stream(Auto.class.getDeclaredFields())
                .map(Field::getName);
        BASE_FIELDS = fieldsStream.get().collect(Collectors.joining(", "));
        CREATE_PLACEHOLDERS = fieldsStream.get()
                .map(s -> ":" + s)
                .collect(Collectors.joining(", "));
        UPDATE_PLACEHOLDERS = fieldsStream.get()
                .filter(s -> !"id".equalsIgnoreCase(s))
                .map(s -> s + "= :" + s)
                .collect(Collectors.joining(", "));
        TABLE_FIELDS = fieldsStream.get().map(s -> s + " TEXT"
                + ("id".equals(s) ? " PRIMARY KEY" : "")
                + " NOT NULL").collect(Collectors.joining(", "));
    }

    private static final String BASE_FIELDS;
    private static final String CREATE_PLACEHOLDERS;
    private static final String UPDATE_PLACEHOLDERS;
    private static final String TABLE_FIELDS;
    public static final String SELECT_QUERY = "SELECT " + BASE_FIELDS + " FROM auto";
    public static final String INSERT_QUERY =
            "INSERT INTO auto(" + BASE_FIELDS + ") VALUES (" + CREATE_PLACEHOLDERS + ")";
    public static final String UPDATE_QUERY =
            "UPDATE auto SET " + UPDATE_PLACEHOLDERS + " where id = :id";
    public static final String DELETE_QUERY = "DELETE FROM auto where id = :id";
    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS auto (" + TABLE_FIELDS + ")";

}
