package br.com.timer.objects;

import br.com.timer.collectors.DBCollector;
import br.com.timer.interfaces.Database;

public class DBCollectors {

    public static <T extends Database> DBCollector<T> create(T type) {
        return new DBCollector<>(type);
    }

}
