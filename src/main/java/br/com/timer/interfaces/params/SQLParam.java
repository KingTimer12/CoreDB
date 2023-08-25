package br.com.timer.interfaces.params;

import br.com.timer.objects.rows.TypeField;

public interface SQLParam {

    TypeField getTypeField();
    int getSize();
    boolean isNull();
    boolean hasAutoIncrement();

}
