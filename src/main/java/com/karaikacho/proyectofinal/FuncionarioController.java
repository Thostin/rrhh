package com.karaikacho.proyectofinal;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import InfoTrespassing.ObjetosEstaticos;
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
public class FuncionarioController implements Initializable {

    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtLineaBaja;
    @FXML
    private TextField txtCelular;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (ObjetosEstaticos.reloadFuncionario != null) {
            txtNombres.setText(ObjetosEstaticos.reloadFuncionario.getNombres());
            txtApellidos.setText(ObjetosEstaticos.reloadFuncionario.getApellidos());
            txtCedula.setText(ObjetosEstaticos.reloadFuncionario.getCi());
            txtLineaBaja.setText(ObjetosEstaticos.reloadFuncionario.getLineaBaja());
            txtCelular.setText(ObjetosEstaticos.reloadFuncionario.getCelular());
        }
    }

    @FXML
    private void confirmar(ActionEvent event) {
        ObjetosEstaticos.funcionario.setApellidos(txtApellidos.getText());
        ObjetosEstaticos.funcionario.setNombres(txtNombres.getText());
        ObjetosEstaticos.funcionario.setCi(txtCedula.getText());
        ObjetosEstaticos.funcionario.setLineaBaja(txtLineaBaja.getText());
        ObjetosEstaticos.funcionario.setCelular(txtCelular.getText());

        ObjetosEstaticos.confirmado = true;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ObjetosEstaticos.confirmado = false;

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
