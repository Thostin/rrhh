/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbEspecialidad;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class EspecialidadesController extends OpensFXML implements Initializable {

    ArrayList<DbEspecialidad> registrosBase = null;
    ObservableList<DbEspecialidad> registros = null;
    ObservableList<DbEspecialidad> registrosFiltrados = null;

    @FXML
    private TableView<DbEspecialidad> tablaEspecialidades;
    @FXML
    private TableColumn<DbEspecialidad, String> columnaId;
    @FXML
    private TableColumn<DbEspecialidad, String> columnaNombre;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnSalir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarDatos();
    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(registrosBase = DbEspecialidad.readAll());
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaEspecialidades.setItems(registros);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        abrirFXML("especialidad.fxml", "Anadir Especialidad", (Stage) ((Node) event.getSource()).getScene().getWindow());
        DbEspecialidad especialidad;
        if (ObjetosEstaticos.confirmado == true) {
            ObjetosEstaticos.confirmado = false;
            especialidad = new DbEspecialidad(ObjetosEstaticos.especialidad.getNombre());
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("El sistema comunica: ");
            alerta.setHeaderText("");

            if (especialidad.insert()) {
                alerta.setContentText("Datos insertados correctamente");
                alerta.show();
            } else {
                alerta.setContentText("Hubo un error al insertar los datos");
                alerta.show();
            }
            tablaEspecialidades.getItems().add(especialidad);
            registrosBase.add(especialidad);
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        DbEspecialidad especialidadSeleccionadaEliminar = tablaEspecialidades.getSelectionModel().getSelectedItem();
        if (null != especialidadSeleccionadaEliminar) {
            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Aviso de borrado");
            confirmar.setHeaderText("");
            confirmar.setContentText("¿Estas seguro de eliminar la especialidad '" + especialidadSeleccionadaEliminar.getNombre() + "'?");

            if (confirmar.showAndWait().get() == ButtonType.OK) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                //tablaFuncionarios.getItems().remove(especialidadSeleccionadaEliminar);
                registrosBase.remove(especialidadSeleccionadaEliminar);
                tablaEspecialidades.setItems(FXCollections.observableArrayList(registrosBase));
                if (especialidadSeleccionadaEliminar.delete()) {
                    alerta.setContentText("La especialidad '" + especialidadSeleccionadaEliminar.getNombre() + "' fue eliminada de la base de datos satisfactoriamente");
                } else {
                    alerta.setContentText("La especialidad '" + especialidadSeleccionadaEliminar.getNombre() + "' no pudo ser eliminada de la base de datos");
                }

                alerta.show();
            }
        }
    }

    @FXML
    private void salir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void modificar(ActionEvent event) {
        DbEspecialidad especialidadSeleccionadaModificar = tablaEspecialidades.getSelectionModel().getSelectedItem();

        if (null != especialidadSeleccionadaModificar) {
            ObjetosEstaticos.reloadEspecialidad = especialidadSeleccionadaModificar;
            DbEspecialidad aux = ObjetosEstaticos.especialidad;
            ObjetosEstaticos.especialidad = especialidadSeleccionadaModificar;

            abrirFXML("especialidad.fxml", "Modificar Especialidad", (Stage) ((Node) event.getSource()).getScene().getWindow());
            ObjetosEstaticos.reloadEspecialidad = null;
            ObjetosEstaticos.especialidad = aux;
            tablaEspecialidades.refresh();
            if (ObjetosEstaticos.confirmado == true) {
                ObjetosEstaticos.confirmado = false;
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                if (especialidadSeleccionadaModificar.update()) {
                    alerta.setContentText("Datos actualizados correctamente");
                    alerta.show();
                } else {
                    alerta.setContentText("Hubo un error al insertar los datos");
                    alerta.show();
                }
            }

        }
    }

}
