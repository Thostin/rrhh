/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbEspecialidad;
import clases.DbSala;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class BuscarEspecialidadesController implements Initializable {

    ObservableList<DbEspecialidad> registros = null;

    @FXML
    private TableView<DbEspecialidad> tablaEspecialidades;
    @FXML
    private TableColumn<DbEspecialidad, Integer> columnaId;
    @FXML
    private TableColumn<DbEspecialidad, String> columnaNombre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarDatos();
    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(DbEspecialidad.readAll());
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaEspecialidades.setItems(registros);
    }

    @FXML
    private void guardarSeleccion(MouseEvent event) {
        ObjetosEstaticos.especialidadSeleccion = tablaEspecialidades.getSelectionModel().getSelectedItem();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
