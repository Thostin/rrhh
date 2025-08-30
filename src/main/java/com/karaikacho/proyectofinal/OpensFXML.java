/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.karaikacho.proyectofinal;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author thotstin
 */
public class OpensFXML {

    // ! CHATGPT 
    // Como bloquear la ventana 
    // que abre la presente
    public void abrirFXML(String direccion, String titulo, Stage owner) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(direccion));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);

            stage.sizeToScene(); //  this will set the stage size to match the FXML
            stage.setResizable(false); // optional: prevents resizing
            stage.setScene(new Scene(root));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(owner);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
