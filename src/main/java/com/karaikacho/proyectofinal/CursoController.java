/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbEspecialidad;
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
public class CursoController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnConfirmar;
    @FXML
    private TextField txtEspecialidad;
    @FXML
    private Button btnBuscarEspecialidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(ObjetosEstaticos.reloadCurso != null){
            txtNombre.setText(ObjetosEstaticos.reloadCurso.getNombre());
            txtEspecialidad.setText(ObjetosEstaticos.reloadCurso.getEspecialidad().getNombre());
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ObjetosEstaticos.confirmado = false;
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void confirmar(ActionEvent event) {
        ObjetosEstaticos.curso.setNombre(txtNombre.getText());
        ObjetosEstaticos.curso.setEspecialidad(new DbEspecialidad(ObjetosEstaticos.especialidadSeleccion.getId(), ObjetosEstaticos.especialidadSeleccion.getNombre()));
        ObjetosEstaticos.confirmado = true;
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
    private void buscarEspecialidad(ActionEvent event) {
        abrirFXML("buscarEspecialidades.fxml", "Buscar Especialidades");
        txtEspecialidad.setText(ObjetosEstaticos.especialidadSeleccion.getNombre());
    }
    
}
