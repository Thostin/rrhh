/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbCurso;
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
public class CursosController extends OpensFXML implements Initializable {

    ArrayList<DbCurso> registrosBase = null;
    ObservableList<DbCurso> registros = null;
    ObservableList<DbCurso> registrosFiltrados = null;

    @FXML
    private TableView<DbCurso> tablaCursos;
    @FXML
    private TableColumn<DbCurso, String> columnaId;
    @FXML
    private TableColumn<DbCurso, String> columnaNombre;
    @FXML
    private TableColumn<?, ?> columnaEspecialidad;
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
        registros = FXCollections.observableArrayList(registrosBase = DbCurso.readAll());
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidadNombre"));

        tablaCursos.setItems(registros);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        abrirFXML("curso.fxml", "Anadir Curso", (Stage) ((Node) event.getSource()).getScene().getWindow());
        DbCurso curso;
        if (ObjetosEstaticos.confirmado == true) {
            ObjetosEstaticos.confirmado = false;
            curso = new DbCurso(ObjetosEstaticos.curso.getNombre(), ObjetosEstaticos.especialidadSeleccion.getId());
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("El sistema comunica: ");
            alerta.setHeaderText("");

            if (curso.insert()) {
                alerta.setContentText("Datos insertados correctamente");
                alerta.show();
            } else {
                alerta.setContentText("Hubo un error al insertar los datos");
                alerta.show();
            }
            tablaCursos.getItems().add(curso);
            registrosBase.add(curso);
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        DbCurso cursoSeleccionadaEliminar = tablaCursos.getSelectionModel().getSelectedItem();
        if (null != cursoSeleccionadaEliminar) {
            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Aviso de borrado");
            confirmar.setHeaderText("");
            confirmar.setContentText("¿Estas seguro de eliminar curso '" + cursoSeleccionadaEliminar.getNombre() + "'?");

            if (confirmar.showAndWait().get() == ButtonType.OK) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                //tablaFuncionarios.getItems().remove(cursoSeleccionadaEliminar);
                registrosBase.remove(cursoSeleccionadaEliminar);
                tablaCursos.setItems(FXCollections.observableArrayList(registrosBase));
                if (cursoSeleccionadaEliminar.delete()) {
                    alerta.setContentText("El curso '" + cursoSeleccionadaEliminar.getNombre() + "' fue eliminado de la base de datos satisfactoriamente");
                } else {
                    alerta.setContentText("El curso '" + cursoSeleccionadaEliminar.getNombre() + "' no pudo ser eliminado de la base de datos");
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
        DbCurso cursoSeleccionadaModificar = tablaCursos.getSelectionModel().getSelectedItem();

        if (null != cursoSeleccionadaModificar) {
            ObjetosEstaticos.reloadCurso = cursoSeleccionadaModificar;
            DbCurso aux = ObjetosEstaticos.curso;
            ObjetosEstaticos.curso = cursoSeleccionadaModificar;

            abrirFXML("curso.fxml", "Modificar Curso", (Stage) ((Node) event.getSource()).getScene().getWindow());
            ObjetosEstaticos.reloadCurso = null;
            ObjetosEstaticos.curso = aux;
            tablaCursos.refresh();
            if (ObjetosEstaticos.confirmado == true) {
                ObjetosEstaticos.confirmado = false;
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                if (cursoSeleccionadaModificar.update()) {
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
