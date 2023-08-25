package br.com.timer.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class DataHandler {

    private final Map<String, Data> data;
    @Getter
    private final boolean next;

    public Optional<Data> get(String field) {
        return Optional.ofNullable(data.get(field));
    }

    public void of(BiConsumer<String, Data> rowConsumer) {
        data.forEach(rowConsumer);
    }

}
