package br.com.timer.objects.builders;

import br.com.timer.objects.Data;
import br.com.timer.objects.DataHandler;
import br.com.timer.objects.SQLHandler;
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
    private boolean orderBy;
    private Map<String, OrderType> orderBySpecif;
    private OrderType orderType;
    private int limit;

    public FetchBuilder from(String from) {
        this.from = from;
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

    public FetchBuilder orderBy(OrderType orderType) {
        this.orderBy = true;
        this.orderType = orderType;
        this.orderBySpecif = null;
        return this;
    }

    public FetchBuilder orderBy(OrderType orderType, String param) {
        this.orderBy = true;
        if (this.orderBySpecif == null) {
            this.orderBySpecif = new HashMap<>();
        }
        this.orderBySpecif.put(param, orderType);
        return this;
    }

    public FetchBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public DataHandler builder() {
        final Map<String, Data> rows = new HashMap<>();
        if (filter == null || filter.isEmpty())
            filter = "*";
        StringBuilder query = new StringBuilder("SELECT " + filter + " FROM `" + from + "` ");

        boolean next = false;
        boolean wher = false;
        if (!orderBy) {
            query.append("WHERE `").append(wheres.get(0).getField()).append("`=?");
            if (wheres.size() > 1) for (int i = 1; i < wheres.size(); i++)
                query.append(" AND `").append(wheres.get(i).getField()).append("`").append(wheres.get(i).toStringEncoded());
            wher = true;
        } else {
            query.append("ORDER BY ");
            if (this.orderBySpecif == null) {
                query.append("`").append(wheres.get(0).getField()).append("` ").append(orderType.getSyntax());
                if (wheres.size() > 1) for (int i = 1; i < wheres.size(); i++)
                    query.append(", `").append(wheres.get(i).getField()).append("` ").append(orderType.getSyntax());
            } else {
                int index = 0;
                for (Row where : wheres) {
                    if (orderBySpecif.containsKey(where.getField())) {
                        if (index == 0) {
                            query.append("`").append(where.getField()).append("` ")
                                    .append(orderBySpecif.get(where.getField()).getSyntax());
                        } else {
                            query.append(", `").append(where.getField()).append("` ")
                                    .append(orderBySpecif.get(where.getField()).getSyntax());
                        }
                    }
                    if (index == 0) {
                        query.append("`").append(where.getField()).append("` ")
                                .append(orderType.getSyntax());
                    } else {
                        query.append(", `").append(where.getField()).append("` ")
                                .append(orderType.getSyntax());
                    }
                    index++;
                }
            }
        }

        if (limit != 0) {
            query.append(" LIMIT ").append(limit);
        }

        handler.openConnection();
        try (final PreparedStatement statement = handler.getConnection().prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (wher)
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
        return new DataHandler(rows, next);
    }

}
