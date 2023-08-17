package br.com.timer.collectors;

import br.com.timer.interfaces.DBBackend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DBCollector<T extends DBBackend> {

    @Getter
    private final T handler;

    public DBCollector<T> openConnection() {
        this.handler.openConnection();
        return this;
    }

    public DBCollector<T> closeConnection() {
        this.handler.closeConnection();
        return this;
    }



}
