module org.example.final_di {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens org.example.final_di to javafx.fxml;
    opens restaurante to org.hibernate.orm.core;
    exports restaurante;
    exports org.example.final_di;
}