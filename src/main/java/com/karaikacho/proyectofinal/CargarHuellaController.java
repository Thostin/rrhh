/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.ArduinoLectorHuellas;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class CargarHuellaController extends OpensFXML implements Initializable {

    @FXML
    private Button btnBuscarFuncionario;
    @FXML
    private TextField txtFuncionario;
    @FXML
    private Button btnLeerHuella;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void buscarFuncionario(ActionEvent event) {
        abrirFXML("buscarFuncionarios.fxml", "Seleccionar Funcionario", (Stage) ((Node) event.getSource()).getScene().getWindow());
        txtFuncionario.setText(ObjetosEstaticos.funcionarioSeleccion.getNombres());
    }

    @FXML
    private void leerHuella(ActionEvent event) {
        try {
            ArduinoLectorHuellas.port.getOutputStream().write((ObjetosEstaticos.funcionarioSeleccion.getId()+"").getBytes());
            ArduinoLectorHuellas.port.getOutputStream().flush();
        } catch (IOException ex) {
            System.getLogger(CargarHuellaController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
}
