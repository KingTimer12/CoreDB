package br.com.timer.examples;

import br.com.timer.annotations.ColumnRow;
import br.com.timer.annotations.PrimaryKeyAutoIncrement;
import br.com.timer.annotations.TableName;
import br.com.timer.collectors.DBCollector;
import br.com.timer.objects.HandlerDAO;
import br.com.timer.objects.rows.Rows;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@TableName(name = "exampleDAO")
@EqualsAndHashCode(callSuper = true)
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

    @ColumnRow
    private Roles roles;

    @ColumnRow
    private Date date;

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
