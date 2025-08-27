/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
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
public class BuscarSalasController implements Initializable {

    ObservableList<DbSala> registros = null;

    @FXML
    private TableView<DbSala> tablaSalas;
    @FXML
    private TableColumn<DbSala, Integer> columnaId;
    @FXML
    private TableColumn<DbSala, String> columnaNombre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarDatos();
    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(DbSala.readAll());
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaSalas.setItems(registros);
    }

    @FXML
    private void guardarSeleccion(MouseEvent event) {
        ObjetosEstaticos.salaSeleccion = tablaSalas.getSelectionModel().getSelectedItem();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
