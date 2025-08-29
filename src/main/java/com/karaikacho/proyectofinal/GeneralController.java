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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class GeneralController extends OpensFXML implements Initializable {

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
    
    @FXML
    private void funcionarios(ActionEvent event) {
        btnFuncionarios.setDisable(true);
        abrirFXML("acceso.fxml", "Funcionarios", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnFuncionarios.setDisable(false);
    }

    @FXML
    private void salas(ActionEvent event) {
        btnSalas.setDisable(true);
        abrirFXML("salas.fxml", "Salas", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnSalas.setDisable(false);
    }

    @FXML
    private void especialidades(ActionEvent event) {
        btnEspecialidades.setDisable(true);
        abrirFXML("especialidades.fxml", "Especialidades", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnEspecialidades.setDisable(false);
    }

    @FXML
    private void cursos(ActionEvent event) {
        btnCursos.setDisable(true);
        abrirFXML("cursos.fxml", "Cursos", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnCursos.setDisable(false);
    }

    @FXML
    private void marcar(ActionEvent event) {
        btnMarcar.setDisable(true);
        abrirFXML("marcar.fxml", "Marcar", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnMarcar.setDisable(false);
    }

    @FXML
    private void chequear(ActionEvent event) {
        btnChequear.setDisable(true);
        abrirFXML("chequearFuncionario.fxml", "Chequear", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnChequear.setDisable(false);
    }

}
