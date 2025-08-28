/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbFuncionario;
import clases.FuncionarioChecker;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class ChequearFuncionarioController implements Initializable {

    DbFuncionario funcionarioSeleccionado = null;
    @FXML
    private Button btnChequearFuncionario;
    @FXML
    private TextField txtFuncionario;
    @FXML
    private Button btnRevisar;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void chequear(ActionEvent event) {
        if (null == funcionarioSeleccionado) {
            return;
        }

        FuncionarioChecker.checkHorarioFuncionario(funcionarioSeleccionado, fechaInicio.getValue(), fechaFin.getValue());
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
    private void buscarFuncionario(ActionEvent event) {
        abrirFXML("buscarFuncionarios.fxml", "Buscar Funcionario");
        funcionarioSeleccionado = ObjetosEstaticos.funcionarioSeleccion;
    }

}
