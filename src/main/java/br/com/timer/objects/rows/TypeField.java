package br.com.timer.objects.rows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
public enum TypeField {

    TEXT(String.class),
    DATE(Date.class),
    INT(Integer.class, int.class),
    DOUBLE(Double.class, double.class),
    VARCHAR(UUID.class),
    EMPTY();

    private final Set<Class<?>> aClass = new HashSet<>();

    TypeField(Class<?>... classes) {
        this.aClass.addAll(Set.of(classes));
    }

    public static Optional<TypeField> get(Class<?> aClass) {
       return Arrays.stream(TypeField.values()).filter(t -> t.aClass.contains(aClass)).findAny();
    }

}
