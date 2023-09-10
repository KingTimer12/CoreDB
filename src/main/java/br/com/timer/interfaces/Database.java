package br.com.timer.interfaces;


import br.com.timer.objects.builders.FetchBuilder;
import br.com.timer.objects.rows.Row;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Database {

    long openConnection();
    long closeConnection();

    /***
     * Create new tables with specifications
     *
     * @param table The table name
     * @param rows Each row is part of the column
     *
     * @return The class to which it belongs (Database)
     */
    Database table(String table, Params... rows);

    /***
     * Create new tables with specifications
     *
     * @param tClass The class for DAO structure
     *
     * @return The class to which it belongs (Database)
     */
    Database table(Class<? extends DAO> tClass);

    /***
     * Get the information that is in the database
     *
     * @return The class to which it belongs (DataHandler)
     */
    FetchBuilder fetch();

    /***
     * Insert new values into the table
     *
     * @param table The table name
     * @param rows Each row is part of the column
     *
     * @return The class to which it belongs (Database)
     */
    Database insert(String table, Row... rows);

    /***
     * Insert new values into the table
     *
     * @param table The table name
     * @param rows Each row is part of the column
     *
     * @return The class to which it belongs (Database)
     */
    Database insert(String table, @NotNull List<Row> rows);

    /***
     * Insert new values into the table
     *
     * @param table The table name
     * @param paramsList Each row is part of the column
     * @param whereParams Row that searches for in the database
     *
     * @return The class to which it belongs (Database)
     */
    Database update(String table, @NotNull List<Row> paramsList, @NotNull List<Row> whereParams);

}
