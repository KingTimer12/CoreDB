package br.com.timer;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Rows;
import br.com.timer.objects.rows.TypeField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName(name = "exampleDAO")
public class ExampleDAO extends HandlerDAO {

    @ColumnRow(field = "uuid", typeField = TypeField.VARCHAR, size = 64)
    private UUID uuid;

    @ColumnRow(field = "name", typeField = TypeField.TEXT)
    private String name;

    @ColumnRow(field = "money", typeField = TypeField.DOUBLE)
    private double coins;

    public void save() {
        try {
            this.save(Rows.of("name", this.name));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            this.load(Rows.of("name", this.name));
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DBCollector<?> getDatabase() {
        return CoreDB.mySQLDBCollector;
    }

    @Override
    public HandlerDAO getHandle() {
        return this;
    }

}
