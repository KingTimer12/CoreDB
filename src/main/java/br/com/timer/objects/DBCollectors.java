package br.com.timer.objects;

import br.com.timer.collectors.DBCollector;
import br.com.timer.interfaces.DBBackend;

public class DBCollectors {

    public static <T extends DBBackend> DBCollector<T> openConnection(T type) {
        return new DBCollector<>(type).openConnection();
    }

}
