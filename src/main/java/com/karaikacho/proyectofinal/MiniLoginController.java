/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import clases.ConnectionPool;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class MiniLoginController implements Initializable {

    @FXML
    private Button btnAcceder;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContrasena;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void abrirFXML(String direccion, String titulo) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(direccion));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);

            stage.sizeToScene(); //  this will set the stage size to match the FXML
            stage.setResizable(false); // optional: prevents resizing

            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> {
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
                long maxMemory = runtime.maxMemory() / (1024 * 1024);

                System.out.println("Memoria usada: " + usedMemory + " MB / " + maxMemory + " MB");
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void acceder(ActionEvent event) {
        btnAcceder.setDisable(true); // Para evitar un posible bug
        ConnectionPool.init(txtUsuario.getText(), txtContrasena.getText());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        abrirFXML("general.fxml", "Página principal");
    }

}
