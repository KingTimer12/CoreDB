package br.com.timer.objects.rows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public enum TypeField {

    TEXT(String.class), INT(Integer.class), DOUBLE(Double.class), VARCHAR(UUID.class), EMPTY(null);

    private final Class<?> aClass;

    public static Optional<TypeField> get(Class<?> aClass) {
       return Arrays.stream(TypeField.values()).filter(typeField -> typeField.getAClass().isAssignableFrom(aClass)).findFirst();
    }

}
