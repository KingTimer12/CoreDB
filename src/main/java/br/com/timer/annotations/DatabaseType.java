package br.com.timer.annotations;

import br.com.timer.objects.DBType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseType {

    DBType dbType() default DBType.MYSQL;

}
