module com.example.ce316project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires zip4j;
    requires org.apache.commons.io;

    opens com.example.ce316project to javafx.fxml;
    exports com.example.ce316project;
}