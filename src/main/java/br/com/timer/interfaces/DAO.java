package br.com.timer.interfaces;

import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Row;

import java.lang.reflect.InvocationTargetException;

public interface DAO {

    void save(Row... keys);
    void load(Row... key);

    DBCollector<?> getDatabase();
    HandlerDAO getHandle();

}
