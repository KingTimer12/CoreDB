package br.com.timer.objects.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Getter
@RequiredArgsConstructor
public abstract class DataHandler implements IDataHandler {

    private final boolean next;

}
