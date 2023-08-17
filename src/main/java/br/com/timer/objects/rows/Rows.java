package br.com.timer.objects.rows;

public class Rows {

    public static Row of(String key, Object value) {
        return new Row(key, value);
    }

    public static RowCreate of(String key, TypeField typeField) {
        return new RowCreate(key, typeField, 0, false);
    }

    public static RowCreate of(String key, TypeField typeField, boolean isNull) {
        return new RowCreate(key, typeField, 0, isNull);
    }

    public static RowCreate of(String key, TypeField typeField, int size) {
        return new RowCreate(key, typeField, size, false);
    }

    public static RowCreate of(String key, TypeField typeField, int size, boolean isNull) {
        return new RowCreate(key, typeField, size, isNull);
    }

}
