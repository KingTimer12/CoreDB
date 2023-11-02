package br.com.timer.objects.builders;

import br.com.timer.annotations.TableName;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.data.Data;
import br.com.timer.objects.data.DataHandler;
import br.com.timer.objects.SQLHandler;
import br.com.timer.objects.data.types.ListData;
import br.com.timer.objects.rows.Row;
import br.com.timer.objects.rows.RowList;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
public class ListBuilder {

    private final SQLHandler handler;

    private String from, filter;
    private final List<RowList> wheres = new ArrayList<>();
    private boolean orderBy;
    private Map<String, OrderType> orderBySpecif;
    private OrderType orderType;
    private int limit;

    public ListBuilder from(String from) {
        this.from = from;
        return this;
    }

    public ListBuilder from(Class<? extends HandlerDAO> from) {
        TableName table = from.getAnnotation(TableName.class);
        if (table != null) {
            this.from = table.name();
        }
        return this;
    }

    public ListBuilder filter(String filter) {
        this.filter = filter;
        return this;
    }

    public ListBuilder where(RowList where) {
        this.wheres.add(where);
        return this;
    }

    public ListBuilder where(RowList... where) {
        this.wheres.addAll(List.of(where));
        return this;
    }

    public ListBuilder orderBy(OrderType orderType) {
        this.orderBy = true;
        this.orderType = orderType;
        this.orderBySpecif = null;
        return this;
    }

    public ListBuilder orderBy(OrderType orderType, String param) {
        this.orderBy = true;
        if (this.orderBySpecif == null) {
            this.orderBySpecif = new HashMap<>();
        }
        this.orderBySpecif.put(param, orderType);
        return this;
    }

    public ListBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public ListData builder() {
        final List<Map<String, Data>> items = Collections.synchronizedList(new ArrayList<>());
        if (filter == null || filter.isEmpty())
            filter = "*";
        StringBuilder query = new StringBuilder("SELECT " + filter + " FROM `" + from + "` ");
        if (orderBy) {
            query.append("ORDER BY ");
            if (this.orderBySpecif == null) {
                query.append("`").append(wheres.get(0).getField()).append("` ").append(orderType.getSyntax());
                if (wheres.size() > 1) for (int i = 1; i < wheres.size(); i++)
                    query.append(", `").append(wheres.get(i).getField()).append("` ").append(orderType.getSyntax());
            } else {
                int index = 0;
                for (RowList where : wheres) {
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

        System.out.println(query);

        handler.openConnection();
        boolean next = false;
        try (final PreparedStatement statement = handler.getConnection().prepareStatement(query.toString())) {
            try (final ResultSet resultSet = statement.executeQuery()) {
                final ResultSetMetaData metaData = resultSet.getMetaData();
                final int columnCount = metaData.getColumnCount();
                int stop = -1;
                while (resultSet.next()) {
                    next = true;
                    final Map<String, Data> listData = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        try {
                            if (stop != -1 && stop == i)
                                break;
                            final Object object = resultSet.getObject(i);
                            listData.put(metaData.getColumnName(i), new Data(object));
                        } catch (Exception ignored) {
                            stop = i;
                            break;
                        }
                    }
                    items.add(listData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            handler.closeConnection();
        }
        return new ListData(items, next);
    }

}
