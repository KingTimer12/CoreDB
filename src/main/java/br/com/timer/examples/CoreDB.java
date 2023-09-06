package br.com.timer.examples;

import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.DBCollectors;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;

public class CoreDB {

    public static DBCollector<MySQL> mySQLDBCollector;

    public static void main(String[] args) {
        mySQLDBCollector = DBCollectors.create(new MySQL("localhost", 3306, "root", "", "test"));
        mySQLDBCollector.getHandler().table(ExampleDAO.class);

        ExampleDAO exampleDAO = new ExampleDAO();
        exampleDAO.load(Rows.of("name", "Marcos"));
        System.out.println(exampleDAO.getCoins());
    }

}
