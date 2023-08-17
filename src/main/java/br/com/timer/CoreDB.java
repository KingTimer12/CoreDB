package br.com.timer;

import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.DBCollectors;
import br.com.timer.types.MySQL;

public class CoreDB {

    public static void main(String[] args) {
        DBCollector<MySQL> mySQLDBCollector = DBCollectors.openConnection(new MySQL("localhost", 3306, "root", "", "test"));
        mySQLDBCollector.getHandler().table(ExampleDAO.class);
    }

}
