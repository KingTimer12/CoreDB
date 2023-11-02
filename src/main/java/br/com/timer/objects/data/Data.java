package br.com.timer.objects.data;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class Data {

    private final Object object;

    public String asString() {
        return String.valueOf(object);
    }

    public byte asByte() {
        return Byte.parseByte(asString());
    }

    public int asInt() {
        return Integer.parseInt(asString());
    }

    public double asDouble() {
        return Double.parseDouble(asString());
    }

    public long asLong() {
        return Long.parseLong(asString());
    }

    public UUID asUUID() {
        return UUID.fromString(asString());
    }

    public <T> T asOther(T other) {
        return (T) object;
    }

}
