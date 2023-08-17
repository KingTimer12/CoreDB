package br.com.timer;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.TableName;
import br.com.timer.objects.rows.TypeField;

import java.util.UUID;

@TableName(name = "exampleDAO")
public class ExampleDAO {

    @ColumnRow(field = "uuid", typeField = TypeField.VARCHAR)
    private UUID uuid;

    @ColumnRow(field = "name", typeField = TypeField.TEXT)
    private String name;

    @ColumnRow(field = "money", typeField = TypeField.INT)
    private double coins;

}
