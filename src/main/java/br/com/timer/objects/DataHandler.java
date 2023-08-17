package br.com.timer.objects;

import br.com.timer.objects.rows.Row;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class DataHandler {

    private final List<Row> rows;
    @Getter
    private final boolean next;

    public void of(Consumer<Row> consumer) {
        of(null, consumer);
    }

    public void of(String field, Consumer<Row> consumer) {
        if (field == null)
            rows.forEach(consumer);
        rows.stream().filter(f -> f.getField().equalsIgnoreCase(field)).forEach(consumer);
    }

}
