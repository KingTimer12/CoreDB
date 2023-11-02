package br.com.timer.objects.builders;

import br.com.timer.annotations.TableName;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.data.Data;
import br.com.timer.objects.data.DataHandler;
import br.com.timer.objects.SQLHandler;
import br.com.timer.objects.data.types.FetchData;
import br.com.timer.objects.rows.Row;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
public class FetchBuilder {

    private final SQLHandler handler;

    private String from, filter;
    private final List<Row> wheres = new ArrayList<>();
    private int limit;

    public FetchBuilder from(String from) {
        this.from = from;
        return this;
    }

    public FetchBuilder from(Class<? extends HandlerDAO> from) {
        TableName table = from.getAnnotation(TableName.class);
        if (table != null) {
            this.from = table.name();
        }
        return this;
    }

    public FetchBuilder filter(String filter) {
        this.filter = filter;
        return this;
    }

    public FetchBuilder where(Row where) {
        this.wheres.add(where);
        return this;
    }

    public FetchBuilder where(Row... where) {
        this.wheres.addAll(List.of(where));
        return this;
    }

    public FetchBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public FetchData builder() {
        final Map<String, Data> rows = new HashMap<>();
        if (filter == null || filter.isEmpty())
            filter = "*";
        StringBuilder query = new StringBuilder("SELECT " + filter + " FROM `" + from + "` ");

        boolean next = false;
        query.append("WHERE ").append(wheres.get(0).toStringEncoded());
        if (wheres.size() > 1) for (int i = 1; i < wheres.size(); i++)
            query.append(" AND ").append(wheres.get(i).toStringEncoded());

        if (limit != 0) {
            query.append(" LIMIT ").append(limit);
        }
        System.out.println(query.toString());

        handler.openConnection();
        try (final PreparedStatement statement = handler.getConnection().prepareStatement(query.toString())) {
            int paramIndex = 1;
            for (Row where : wheres) {
                statement.setObject(paramIndex++, where.getDefaultValue());
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
                            rows.put(metaData.getColumnName(i), new Data(resultSet.getObject(i)));
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
            handler.closeConnection();
        }
        return new FetchData(rows, next);
    }

}
