package br.com.timer.objects.data.types;

import br.com.timer.objects.data.Data;
import br.com.timer.objects.data.DataHandler;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
public class ListData extends DataHandler {

    private final List<Map<String, Data>> data;

    public ListData(List<Map<String, Data>> data, boolean next) {
        super(next);
        this.data = data;
    }

    @Override
    public Optional<List<Data>> get(String field) {
        return Optional.of(this.data.stream()
                .filter(stringDataMap -> stringDataMap.containsKey(field))
                .map(stringDataMap -> stringDataMap.get(field))
                .collect(Collectors.toList()));
    }

    public void of(Consumer<? super Map<String, Data>> rowConsumer) {
        data.forEach(rowConsumer);
    }

}
