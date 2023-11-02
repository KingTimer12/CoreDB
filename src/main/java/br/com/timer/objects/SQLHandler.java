package br.com.timer.objects;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.interfaces.Database;
import br.com.timer.interfaces.DAO;
import br.com.timer.interfaces.Params;
import br.com.timer.interfaces.params.SQLParam;
import br.com.timer.objects.builders.FetchBuilder;
import br.com.timer.objects.builders.ListBuilder;
import br.com.timer.objects.rows.Row;
import br.com.timer.objects.rows.RowCreate;
import br.com.timer.objects.rows.Rows;
import br.com.timer.objects.rows.TypeField;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Getter
public abstract class SQLHandler implements Database {

    protected int query = 0;
    protected Connection connection;

    @Override
    public SQLHandler table(String table, Params... rows) {
        return table(table, Arrays.stream(rows).map(row -> (RowCreate) row).collect(Collectors.toList()));
    }

    @Override
    @SneakyThrows
    public SQLHandler table(Class<? extends DAO> tClass) {
        TableName tableName = tClass.getAnnotation(TableName.class);
        List<RowCreate> rows = new ArrayList<>();
        for (Field field : tClass.getDeclaredFields()) {
            ColumnRow columnRow = field.getAnnotation(ColumnRow.class);
            if (columnRow != null) {
                String fieldName = columnRow.field();
                if (Objects.equals(fieldName, "null")) {
                    fieldName = field.getName();
                }

                PrimaryKeyAutoIncrement primaryKeyAutoIncrement = field.getAnnotation(PrimaryKeyAutoIncrement.class);
                if (primaryKeyAutoIncrement != null) {
                    rows.add(Rows.of(fieldName, TypeField.INT, 0, 0, false, true));
                    continue;
                }

                TypeField fieldType = columnRow.typeField();
                if (fieldType.equals(TypeField.EMPTY)) {
                    fieldType = !field.getType().isEnum() ? TypeField.get(field.getType()).orElse(TypeField.EMPTY) : TypeField.TEXT;
                }

                field.setAccessible(true);
                rows.add(Rows.of(fieldName, fieldType, columnRow.size(), null, columnRow.isNull()));
            }
        }
        return table(tableName.name(), rows);
    }

    @Override
    public FetchBuilder fetch() {
        return new FetchBuilder(this);
    }

    @Override
    public ListBuilder list() {
        return new ListBuilder(this);
    }

    @Override
    public SQLHandler insert(String table, Row... rows) {
        return insert(table, Arrays.asList(rows));
    }

    @Override
    public SQLHandler insert(String table, @NotNull List<Row> rows) {
        final List<String> paramFields = rows.stream().map(Row::getField).map(t -> "`"+t+"`").collect(Collectors.toList());
        final List<String> paramValues = rows.stream().map(Row::getValue).map(String::valueOf).map(t -> "?").collect(Collectors.toList());

        final String paramFieldsSplit = String.join(",", paramFields);
        final String paramValuesSplit = String.join(",", paramValues);

        final String stmt = "INSERT INTO `" + table + "`" +
                " (" + paramFieldsSplit + ") VALUES (" + paramValuesSplit + ")";
        executeAction(stmt, rows, null);
        return this;
    }

    @Override
    public SQLHandler update(String table, @NotNull List<Row> paramsList, @NotNull List<Row> whereParams) {
        final List<String> whereParamsField = whereParams.stream().map(Row::toStringEncoded).collect(Collectors.toList());
        final List<String> paramsListField = paramsList.stream().map(Row::toStringEncoded).collect(Collectors.toList());

        final String paramsListSplit = String.join(",", paramsListField);
        final String whereParamsSplit = String.join(",", whereParamsField);

        final String stmt = "UPDATE " + table + " SET " + paramsListSplit + " WHERE " + whereParamsSplit;
        executeAction(stmt, paramsList, whereParams);
        return this;
    }

    private SQLHandler table(String table, @NotNull List<? extends SQLParam> rows) {
        final StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + table + "` (");
        if (!rows.isEmpty()) {
            int index = 1;
            for (RowCreate row : rows.stream().map(row -> (RowCreate) row).toList()) {
                builder.append("`").append(row.getKey()).append("` ");
                if (!row.hasAutoIncrement()) {
                    if (row.getSize() == 0) {
                        builder.append(row.getTypeField().name());
                    } else {
                        builder.append(row.getTypeField().name()).append("(")
                                .append(row.getSize()).append(")");
                    }
                    if (row.getDefaultValue() == null) {
                        if (!row.isNull()) {
                            builder.append(" NOT NULL");
                        }
                    } else {
                        builder.append(" DEFAULT ").append(row.getDefaultValue());
                    }
                } else {
                    builder.append(" INT PRIMARY KEY AUTO_INCREMENT");
                }
                if (index < rows.size()) {
                    builder.append(", ");
                }
                index++;
            }
        }
        builder.append(") ENGINE = InnoDB DEFAULT CHARSET = UTF8;");

        System.out.println(builder.toString());

        final long start = openConnection();
        try (final Statement ps = connection.createStatement()) {
            ps.execute(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            final long end = closeConnection();
            long elapsedTime = end - start;
            double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000.0;
            System.out.println("Process duration: " + elapsedTimeInSeconds + " seconds");
        }
        return this;
    }

    private void executeAction(String statment, List<Row> paramsList, @Nullable List<Row> conditions) {
        final long start = openConnection();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(statment, RETURN_GENERATED_KEYS)) {
            convertValue(preparedStatement, paramsList, conditions);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            final long end = closeConnection();
            long elapsedTime = end - start;
            double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000.0;
            System.out.println("Process duration: " + elapsedTimeInSeconds + " seconds");
        }
    }

    private void convertValue(PreparedStatement ps, List<Row> paramsList, @Nullable List<Row> conditions) throws SQLException {
        for (Row v : paramsList) {
            ps.setObject(paramsList.indexOf(v)+1, v.getValue());
        }

        if (conditions != null) {
            int lastValue = paramsList.size();
            for (Row condition : conditions) {
                int index = lastValue + 1;
                ps.setObject(index, condition.getValue());
            }
        }
    }


}
