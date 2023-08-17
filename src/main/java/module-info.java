module br.com.timer {
    exports br.com.timer;
    exports br.com.timer.annotations;
    exports br.com.timer.collectors;
    exports br.com.timer.interfaces;
    exports br.com.timer.objects;
    exports br.com.timer.types;
    exports br.com.timer.objects.rows;

    requires org.apiguardian.api;
    requires static lombok;
    requires org.jetbrains.annotations;
    requires org.junit.platform.engine;
    requires org.junit.platform.commons;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires rt;
    requires bson;
    requires mongodb.driver;
}