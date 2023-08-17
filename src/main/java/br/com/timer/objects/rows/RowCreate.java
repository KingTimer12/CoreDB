package br.com.timer.objects.rows;

import br.com.timer.interfaces.Params;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RowCreate implements Params {

    private final String key;
    private final TypeField typeField;
    private final int size;
    private final boolean isNull;

}
