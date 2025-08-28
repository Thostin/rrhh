/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class GeneralController implements Initializable {

    @FXML
    private Button btnFuncionarios;
    @FXML
    private Button btnSalas;
    @FXML
    private Button btnEspecialidades;
    @FXML
    private Button btnCursos;
    @FXML
    private Button btnMarcar;
    @FXML
    private Button btnChequear;

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
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void funcionarios(ActionEvent event) {
        abrirFXML("acceso.fxml", "Funcionarios");
    }

    @FXML
    private void salas(ActionEvent event) {
        abrirFXML("salas.fxml", "Salas");
    }

    @FXML
    private void especialidades(ActionEvent event) {
        abrirFXML("especialidades.fxml", "Especialidades");
    }

    @FXML
    private void cursos(ActionEvent event) {
        abrirFXML("cursos.fxml", "Cursos");
    }

    @FXML
    private void marcar(ActionEvent event) {
        abrirFXML("marcar.fxml", "Marcar");
    }

    @FXML
    private void chequear(ActionEvent event) {
        abrirFXML("chequearFuncionario.fxml", "Chequear");
    }

}
