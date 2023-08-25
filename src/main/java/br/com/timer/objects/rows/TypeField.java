package br.com.timer.objects.rows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
public enum TypeField {

    TEXT(String.class), INT(Integer.class, int.class), DOUBLE(Double.class, double.class), VARCHAR(UUID.class), EMPTY();

    private final List<Class<?>> aClass = new ArrayList<>();

    TypeField(Class<?>... classes) {
        this.aClass.addAll(Arrays.asList(classes));
    }

    public static Optional<TypeField> get(Class<?> aClass) {
       return Arrays.stream(TypeField.values()).filter(t -> t.aClass.contains(aClass)).findAny();
    }

}
