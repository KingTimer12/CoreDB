package br.com.timer.objects.data.types;

import br.com.timer.objects.data.Data;
import br.com.timer.objects.data.DataHandler;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Getter
public class FetchData extends DataHandler {

    private final Map<String, Data> data;

    public FetchData(Map<String, Data> data, boolean next) {
        super(next);
        this.data = data;
    }

    @Override
    public Optional<Data> get(String field) {
        return Optional.ofNullable(this.data.get(field));
    }

    public void of(BiConsumer<String, Data> rowConsumer) {
        data.forEach(rowConsumer);
    }

}
