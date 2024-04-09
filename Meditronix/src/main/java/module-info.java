module com.example.meditronix {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    //requires mysql.connector;

    opens com.example.meditronix to javafx.fxml;
    exports com.example.meditronix;
}