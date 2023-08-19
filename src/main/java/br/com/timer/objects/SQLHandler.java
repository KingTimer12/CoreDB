package br.com.timer.objects;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.interfaces.DBBackend;
import br.com.timer.interfaces.DAO;
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
public abstract class SQLHandler implements DBBackend {

    protected int query = 0;
    protected Connection connection;

    /***
     * Create new tables with specifications
     *
     * @param table The table name
     * @param rows Each row is part of the column
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public SQLHandler table(String table, RowCreate... rows) {
        return table(table, Set.of(rows));
    }

    /***
     * Create new tables with specifications
     *
     * @param tClass The class for DAO structure
     *
     * @return The class to which it belongs (SQLHandler)
     */
    @SneakyThrows
    public SQLHandler table(Class<? extends DAO> tClass) {
        TableName tableName = tClass.getAnnotation(TableName.class);
        Set<RowCreate> rows = new HashSet<>();
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
                    fieldType = TypeField.get(field.getDeclaringClass()).orElse(TypeField.EMPTY);
                }

                field.setAccessible(true);
                rows.add(Rows.of(fieldName, fieldType, columnRow.size(), null, columnRow.isNull()));
            }
        }
        return table(tableName.name(), rows);
    }

    /***
     * Get the information that is in the database
     *
     * @param from The table name
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public DataHandler fetch(String from) {
        return fetch(from, "*", (Row) null);
    }

    /***
     * Get the information that is in the database
     *
     * @param from The table name
     * @param where Condition for the search
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public DataHandler fetch(String from, Row... where) {
        return fetch(from, "*", where);
    }

    /***
     * Insert new values into the table
     *
     * @param table The table name
     * @param rows Each row is part of the column
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public SQLHandler insert(String table, Row... rows) {
        return insert(table, Arrays.asList(rows));
    }

    /***
     * Insert new values into the table
     *
     * @param table The table name
     * @param rows Each row is part of the column
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public SQLHandler insert(String table, @NotNull List<Row> rows) {
        final List<String> paramFields = rows.stream().map(Row::getField).map(t -> t = "`"+t+"`").collect(Collectors.toList());
        final List<String> paramValues = rows.stream().map(Row::getValue).map(String::valueOf).map(t -> t = "?").collect(Collectors.toList());

        final String paramFieldsSplit = String.join(",", paramFields);
        final String paramValuesSplit = String.join(",", paramValues);

        final String stmt = "INSERT INTO `" + table + "`" +
                " (" + paramFieldsSplit + ") VALUES (" + paramValuesSplit + ")";
        executeAction(stmt, rows, null);
        return this;
    }

    /***
     * Insert new values into the table
     *
     * @param table The table name
     * @param paramsList Each row is part of the column
     * @param whereParams Row that searches for in the database
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public SQLHandler update(String table, @NotNull List<Row> paramsList, @NotNull List<Row> whereParams) {
        final List<String> whereParamsField = whereParams.stream().map(Row::toStringEncoded).collect(Collectors.toList());
        final List<String> paramsListField = paramsList.stream().map(Row::toStringEncoded).collect(Collectors.toList());

        final String paramsListSplit = String.join(",", paramsListField);
        final String whereParamsSplit = String.join(",", whereParamsField);

        final String stmt = "UPDATE " + table + " SET " + paramsListSplit + " WHERE " + whereParamsSplit;
        executeAction(stmt, paramsList, whereParams);
        return this;
    }

    /***
     * Create new tables with specifications
     *
     * @param table The table name
     * @param rows Each row is part of the column
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public SQLHandler table(String table, @org.jetbrains.annotations.NotNull Set<RowCreate> rows) {
        final StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + table + "` (");
        if (!rows.isEmpty()) {
            int index = 1;
            for (RowCreate row : rows) {
                builder.append("`").append(row.getKey()).append("` ");
                if (!row.isAutoIncrement()) {
                    if (row.getSize() == 0) {
                        builder.append(row.getTypeField().name());
                    } else {
                        builder.append(row.getTypeField().name()).append("(").append(row.getSize()).append(")");
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

        openConnection();
        try (final Statement ps = connection.createStatement()) {
            ps.execute(builder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return this;
    }

    /***
     * Get the information that is in the database
     *
     * @param from The table name
     * @param filter Each row is part of the column
     * @param wheres Condition for the search
     *
     * @return The class to which it belongs (SQLHandler)
     */
    public DataHandler fetch(String from, String filter, Row... wheres) {
        final List<Row> rows = new ArrayList<>();
        if (filter == null || filter.equals(""))
            filter = "*";
        StringBuilder query = new StringBuilder("SELECT " + filter + " FROM `" + from + "` ");

        boolean next = false;
        boolean wher = false;
        if (wheres != null) {
            query.append("WHERE `").append(wheres[0].getField()).append("`=?");
            if (wheres.length > 1) for (int i = 1; i < wheres.length; i++)
                query.append(" AND `").append(wheres[i].getField()).append("`=?");
            wher = true;
        }
        openConnection();
        try (final PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (wher)
                for (Row where : wheres) {
                    statement.setObject(paramIndex++, where.getValue());
                }
            try (final ResultSet resultSet = statement.executeQuery()) {
                final ResultSetMetaData metaData = resultSet.getMetaData();
                int stop = -1;
                while (resultSet.next()) {
                    next = true;
                    int i = 1;
                    while (true) {
                        try {

                            if (stop != -1 && stop == i)
                                break;

                            rows.add(Rows.of(metaData.getColumnName(i), resultSet.getObject(i)));
                        } catch (SQLException ignored) {
                            stop = i;
                            break;
                        }
                        i++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return new DataHandler(rows, next);
    }

    private void executeAction(String statment, List<Row> paramsList, @Nullable List<Row> conditions) {
        openConnection();
        try (final PreparedStatement preparedStatement = connection.prepareStatement(statment, RETURN_GENERATED_KEYS)) {
            convertValue(preparedStatement, paramsList, conditions);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void convertValue(PreparedStatement ps, List<Row> paramsList, @Nullable List<Row> conditions) throws SQLException {
        for (Row v : paramsList) {
            final int index = paramsList.indexOf(v) + 1;
            ps.setObject(index, v.getValue());
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
