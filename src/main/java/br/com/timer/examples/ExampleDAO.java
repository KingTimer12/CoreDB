package br.com.timer.examples;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Rows;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName(name = "exampleDAO")
public class ExampleDAO extends HandlerDAO {

    @ColumnRow
    @PrimaryKeyAutoIncrement
    private int id;

    @ColumnRow(size = 64)
    private UUID uuid;

    @ColumnRow
    private String name;

    @ColumnRow
    private double coins;

    public void save() {
        this.save(Rows.of("name", this.name));
    }

    public void load() {
        this.load(Rows.of("name", this.name));
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
