package br.com.timer.objects.rows;

import br.com.timer.interfaces.Params;
import br.com.timer.interfaces.params.SQLParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RowCreate implements Params, SQLParam {

    private final String key;
    private final TypeField typeField;
    private final int size;
    private final Object defaultValue;
    private final boolean isNull;
    private final boolean autoIncrement;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public TypeField getTypeField() {
        return typeField;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isNull() {
        return isNull;
    }

    @Override
    public boolean hasAutoIncrement() {
        return autoIncrement;
    }
}
