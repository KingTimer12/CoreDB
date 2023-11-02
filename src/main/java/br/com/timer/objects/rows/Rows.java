package br.com.timer.objects.rows;

public class Rows {

    public static Row of(String key, Object value) {
        return new Row(key, value);
    }

    public static RowList of(String key) {
        return new RowList(key);
    }

    public static RowCreate of(String key, TypeField typeField) {
        return new RowCreate(key, typeField, 0, null, false, false);
    }

    public static RowCreate of(String key, TypeField typeField, boolean isNull) {
        return new RowCreate(key, typeField, 0, null, isNull, false);
    }

    public static RowCreate of(String key, TypeField typeField, int size) {
        return new RowCreate(key, typeField, size, null, false, false);
    }

    public static RowCreate of(String key, TypeField typeField, int size, Object defaultValue) {
        return new RowCreate(key, typeField, size, defaultValue, false, false);
    }

    public static RowCreate of(String key, TypeField typeField, int size, Object defaultValue, boolean isNull) {
        return new RowCreate(key, typeField, size, defaultValue, isNull, false);
    }

    public static RowCreate of(String key, TypeField typeField, int size, Object defaultValue, boolean isNull, boolean autoIncrement) {
        return new RowCreate(key, typeField, size, defaultValue, isNull, autoIncrement);
    }

}
