package br.com.timer.interfaces;

import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Row;

import java.lang.reflect.InvocationTargetException;

public interface DAO {

    void save(Row... keys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException;
    void load(Row... key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException;

    DBCollector<?> getDatabase();
    HandlerDAO getHandle() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

}
