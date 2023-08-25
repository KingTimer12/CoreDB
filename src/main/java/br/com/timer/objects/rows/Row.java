package br.com.timer.objects.rows;

import br.com.timer.interfaces.Params;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Row implements Params {

    private final String field;
    private final Object value;

    public String toStringEncoded() {
        return field + "=?";
    }

    @Override
    public String getKey() {
        return field;
    }

    @Override
    public Object getDefaultValue() {
        return value;
    }
}
