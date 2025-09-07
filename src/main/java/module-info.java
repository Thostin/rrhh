module com.karaikacho.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires java.base;
    requires com.zaxxer.hikari;
    requires jasperreports;
    requires java.desktop;
    
    opens com.karaikacho.proyectofinal to javafx.fxml, java.desktop;
    opens clases to javafx.fxml, javafx.base;

    exports com.karaikacho.proyectofinal;
    exports clases;
}
