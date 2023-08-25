package br.com.timer.types;

import br.com.timer.objects.SQLHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class MariaDB extends SQLHandler {

    private final String host;
    private final String username;
    private final String password;
    private final String database;

    @Override
    public long openConnection() {
        try {
            query++;
            if ((connection != null) && (!connection.isClosed()))
                return System.nanoTime();

            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://"+host+"/" + database, username, password);
        } catch(SQLException | ClassNotFoundException e) {
            query--;
            System.out.println( "ERROR IN MARIADB!");
            e.getStackTrace();
            e.printStackTrace();
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
            } catch (Exception e) {
                System.out.println(
                        "Houve um erro ao fechar a conexão!");
            }
        }
        return System.nanoTime();
    }
}
