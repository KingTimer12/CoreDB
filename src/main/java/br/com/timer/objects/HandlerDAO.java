package br.com.timer.objects;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.interfaces.DAO;
import br.com.timer.objects.rows.Row;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RequiredArgsConstructor
public abstract class HandlerDAO implements DAO {

    @Override
    public void save(Row... key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        DBCollector<?> dbCollector = getDatabase();
        HandlerDAO handlerDAO = getHandle();
        Class<? extends HandlerDAO> tableClass = handlerDAO.getClass();
        if (dbCollector == null) return;

        List<Row> values = new ArrayList<>();
        for (Field field : tableClass.getDeclaredFields()) {
            ColumnRow columnRow = field.getAnnotation(ColumnRow.class);
            if (columnRow != null) {
                PrimaryKeyAutoIncrement primaryKeyAutoIncrement = field.getAnnotation(PrimaryKeyAutoIncrement.class);
                if (primaryKeyAutoIncrement != null) continue;
                String fieldName = columnRow.field();
                if (Objects.equals(fieldName, "null")) {
                    fieldName = field.getName();
                }
                field.setAccessible(true);
                Object fieldValue = field.get(handlerDAO);
                if (fieldValue instanceof UUID) {
                    values.add(Rows.of(fieldName, String.valueOf(fieldValue)));
                } else {
                    values.add(Rows.of(fieldName, fieldValue));
                }
            }
        }

        if (dbCollector.getHandler() instanceof MySQL mySQL) {
            String tableName = tableClass.getName();
            TableName table = tableClass.getAnnotation(TableName.class);
            if (table != null) {
                tableName = table.name();
            }

            if (mySQL.fetch(tableName, key).isNext()) {
                List<Row> where = Arrays.asList(key);
                mySQL.update(tableName, values, where);
            } else {
               mySQL.insert(tableName, values);
            }
        }

    }

    @Override
    public void load(Row... key) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DBCollector<?> dbCollector = getDatabase();
        HandlerDAO handlerDAO = getHandle();
        Class<? extends HandlerDAO> tableDAO = handlerDAO.getClass();
        if (dbCollector == null) return;

        Map<String, String> fieldMap = new HashMap<>();
        for (Field field : tableDAO.getDeclaredFields()) {
            ColumnRow columnRow = field.getAnnotation(ColumnRow.class);
            if (columnRow != null) {
                String fieldName = columnRow.field();
                if (Objects.equals(fieldName, "null")) {
                    fieldName = field.getName();
                }
                fieldMap.put(fieldName, field.getName());
            }
        }

        if (dbCollector.getHandler() instanceof MySQL mySQL) {
            String tableName = tableDAO.getName();
            TableName table = tableDAO.getAnnotation(TableName.class);
            if (table != null) {
                tableName = table.name();
            }
            DataHandler dataHandler = mySQL.fetch(tableName, key);
            if (dataHandler.isNext()) {
                dataHandler.of(row -> {
                    String fieldName = fieldMap.get(row.getField());
                    Field field;
                    try {
                        field = tableDAO.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                            field.setDouble(handlerDAO, (Double) row.getValue());
                        } else if (field.getType().equals(UUID.class)) {
                            field.set(handlerDAO, UUID.fromString((String) row.getValue()));
                        } else {
                            field.set(handlerDAO, row.getValue());
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

}
