module br.com.timer {
    exports br.com.timer;
    exports br.com.timer.annotations;
    exports br.com.timer.collectors;
    exports br.com.timer.interfaces;
    exports br.com.timer.objects;
    exports br.com.timer.types;
    exports br.com.timer.objects.rows;

    requires static lombok;
    requires org.jetbrains.annotations;
    requires java.sql;
}