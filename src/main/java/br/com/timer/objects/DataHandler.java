package br.com.timer.objects;

import br.com.timer.objects.rows.Row;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class DataHandler {

    private final List<Row> rows;
    @Getter
    private final boolean next;

    public Optional<Row> get(String field) {
        return rows.stream().filter(f -> f.getField().equalsIgnoreCase(field)).findAny();
    }

    public void of(Consumer<Row> rowConsumer) {
        rows.forEach(rowConsumer);
    }

}
