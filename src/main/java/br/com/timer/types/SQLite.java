package br.com.timer.types;

import br.com.timer.objects.SQLHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@RequiredArgsConstructor
public class SQLite extends SQLHandler {

    private final File storage;

    @Override
    public void openConnection() {
        try {
            query++;
            if ((connection != null) && (!connection.isClosed()))
                return;

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + storage);
        } catch(SQLException | ClassNotFoundException e) {
            query--;
            e.getStackTrace();
            e.printStackTrace();
            System.out.println(
                    "Ocorreu um erro ao abrir a conexão!");
        }
    }

    @Override
    public void closeConnection() {
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
    }
}
