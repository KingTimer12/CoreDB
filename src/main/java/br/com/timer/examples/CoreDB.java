package br.com.timer.examples;

import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.DBCollectors;
import br.com.timer.objects.builders.OrderType;
import br.com.timer.objects.data.Data;
import br.com.timer.objects.data.types.ListData;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class CoreDB {

    public static DBCollector<MySQL> mySQLDBCollector;

    public static void main(String[] args) {
        mySQLDBCollector = DBCollectors.create(new MySQL("localhost",
                3306, "root", "", "test"));
        mySQLDBCollector.getHandler().table(ExampleDAO.class);

        createAccount("Lucas");
        createAccount("Marcos");
        createAccount("Miles");

        updateAccount("Marcos");

        ListData listData = mySQLDBCollector.getHandler().list().from(ExampleDAO.class).where(Rows.of("coins")).orderBy(OrderType.DESC).builder();
        listData.of(stringDataMap -> stringDataMap.forEach((key, value) -> System.out.println(key + " " + value.asString())));

        //System.out.println("Novo: " + exampleDAO.getCoins());
    }

    public static void createAccount(String name) {
        ExampleDAO exampleDAO = new ExampleDAO();
        exampleDAO.setName(name);
        exampleDAO.setUuid(UUID.randomUUID());
        exampleDAO.setRoles(Roles.ADMIN);
        exampleDAO.setCoins(10D);
        exampleDAO.save();
    }

    public static void updateAccount(String name) {
        ExampleDAO exampleDAO = new ExampleDAO();
        exampleDAO.setName(name);
        exampleDAO.load();

        exampleDAO.setCoins(40D);
        exampleDAO.save();
    }

}
