package br.com.timer.types;

import br.com.timer.objects.SQLHandler;
import lombok.RequiredArgsConstructor;

import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
public class MySQL extends SQLHandler {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;

    @Override
    public long openConnection() {
        try {
            query++;
            if ((connection != null) && (!connection.isClosed()))
                return System.nanoTime();

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8", username, password);
        } catch (ClassNotFoundException | SQLException exception) {
            query--;
            exception.printStackTrace();
        }
        return System.nanoTime();
    }

    @Override
    public long closeConnection() {
        query--;
        if (query <= 0) {
            try {
                if (connection != null && !connection.isClosed())
                    connection.close();
                connection = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return System.nanoTime();
    }

}
