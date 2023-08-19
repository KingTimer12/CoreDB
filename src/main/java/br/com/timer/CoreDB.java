package br.com.timer;

import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.DBCollectors;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class CoreDB {

    public static DBCollector<MySQL> mySQLDBCollector;

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        mySQLDBCollector = DBCollectors.create(new MySQL("localhost", 3306, "root", "", "test"));
        mySQLDBCollector.getHandler().table(ExampleDAO.class);
        ExampleDAO exampleDAO = new ExampleDAO();
        exampleDAO.load(Rows.of("name", "Luiz"));
        System.out.println("Coins: " + exampleDAO.getCoins());
    }

}
