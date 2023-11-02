package br.com.timer.objects.data;

import java.util.Optional;

public interface IDataHandler {

    Optional<?> get(String field);

}
