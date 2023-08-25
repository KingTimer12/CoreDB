package br.com.timer.objects.builders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {

    DESC("DESC"), CESC("ASC");

    private final String syntax;

}
