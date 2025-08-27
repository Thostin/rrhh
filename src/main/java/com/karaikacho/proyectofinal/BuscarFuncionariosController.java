/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbFuncionario;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class BuscarFuncionariosController implements Initializable {

    
    ArrayList<DbFuncionario> registrosBase = null;
    ObservableList<DbFuncionario> registros = null;
    ObservableList<DbFuncionario> registrosFiltrados = null;

    @FXML
    private TextField txt_id;
    @FXML
    private TextField txt_nombre;
    @FXML
    private TextField txt_apellido;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<DbFuncionario> tablaFuncionarios;
    @FXML
    private TableColumn<DbFuncionario, Integer> columnaId;
    @FXML
    private TableColumn<DbFuncionario, String> columnaNombres;
    @FXML
    private TableColumn<DbFuncionario, String> columnaApellidos;
    @FXML
    private TableColumn<DbFuncionario, String> columnaCi;
    @FXML
    private TableColumn<DbFuncionario, String> columnaLineaBaja;
    @FXML
    private TableColumn<DbFuncionario, String> columnaCelular;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarDatos();
    }    

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(registrosBase = DbFuncionario.readAll());
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaCi.setCellValueFactory(new PropertyValueFactory<>("ci"));
        columnaLineaBaja.setCellValueFactory(new PropertyValueFactory<>("lineaBaja"));
        columnaCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));

        tablaFuncionarios.setItems(registros);
    }
    @FXML
    private void buscar(KeyEvent event) {
        String busqueda = txtBuscar.getText();
        registrosFiltrados = FXCollections.observableArrayList();
        if (busqueda.isEmpty()) {
            tablaFuncionarios.setItems(registros);
        } else {
            registrosFiltrados.clear();
            for (DbFuncionario registro : registros) {
                if (registro.getNombres().toLowerCase().contains(busqueda.toLowerCase())
                        || registro.getApellidos().toLowerCase().contains(busqueda.toLowerCase())) {
                    registrosFiltrados.add(registro);
                }
            }

            tablaFuncionarios.setItems(registrosFiltrados);
        }
    }

    @FXML
    private void guardarSeleccion(MouseEvent event) {
        ObjetosEstaticos.funcionarioSeleccion = tablaFuncionarios.getSelectionModel().getSelectedItem();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
}
