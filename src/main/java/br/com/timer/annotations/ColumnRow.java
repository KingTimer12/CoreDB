package br.com.timer.annotations;

import br.com.timer.objects.rows.TypeField;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnRow {

    String field() default "null";
    TypeField typeField() default TypeField.EMPTY;
    int size() default 0;
    boolean isNull() default false;

}
