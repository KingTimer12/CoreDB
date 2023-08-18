package br.com.timer;

import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.DBCollectors;
import br.com.timer.objects.rows.Rows;
import br.com.timer.types.MySQL;

import java.util.UUID;

public class CoreDB {

    public static void main(String[] args) {
        DBCollector<MySQL> mySQLDBCollector = DBCollectors.create(new MySQL("localhost", 3306, "root", "", "test"));
        mySQLDBCollector.getHandler().table(ExampleDAO.class);
        String uuid = UUID.randomUUID().toString();
        mySQLDBCollector.getHandler().insert("exampleDAO",
                Rows.of("uuid", uuid),
                Rows.of("name", "Luiz"),
                Rows.of("money", 20));
        mySQLDBCollector.getHandler()
                .fetch("exampleDAO", Rows.of("uuid", uuid), Rows.of("name", "Luiz")).get("money").ifPresent(row -> System.out.println(row.getValue()));
    }

}
