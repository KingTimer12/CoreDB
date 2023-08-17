package br.com.timer.objects.rows;

import br.com.timer.interfaces.Params;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Row implements Params {

    private final String field;
    private final Object value;

    public Object get(String field) {
        return this.field.equalsIgnoreCase(field);
    }

}
