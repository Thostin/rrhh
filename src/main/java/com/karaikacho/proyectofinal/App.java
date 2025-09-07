package com.karaikacho.proyectofinal;

import clases.FuncionarioChecker;
import java.awt.Desktop;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("miniLogin"));
        stage.setScene(scene);
        stage.show();
/*
        if (Desktop.isDesktopSupported()) {
            File aux = new File("/home/thotstin/Code/JAVA/RRHH/ProyectoFinal/Documentation/out/documentacion.pdf");
            Desktop.getDesktop().open(aux);
        } else {
            System.out.println("Desktop API not supported");
        }*/

        /*
        if (ObjetosEstaticos.confirmado == true) {
            ObjetosEstaticos.funcionario.insert();
        } else {
            System.out.println("Se ha cancelado la insercion");
        }*/
        // FuncionarioChecker.checkHorarioFuncionario(23, LocalDate.now().minusDays(1), LocalDate.now());
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
