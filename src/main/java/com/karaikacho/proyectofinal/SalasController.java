/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbSala;
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
public class SalasController implements Initializable {

    ArrayList<DbSala> registrosBase = null;
    ObservableList<DbSala> registros = null;
    ObservableList<DbSala> registrosFiltrados = null;

    @FXML
    private TableView<DbSala> tablaSalas;
    @FXML
    private TableColumn<DbSala, String> columnaId;
    @FXML
    private TableColumn<DbSala, String> columnaNombre;
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
        registros = FXCollections.observableArrayList(registrosBase = DbSala.readAll());
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tablaSalas.setItems(registros);
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
    private void nuevo(ActionEvent event) {
        abrirFXML("sala.fxml", "Anadir Sala");
        DbSala sala;
        if (ObjetosEstaticos.confirmado == true) {
            ObjetosEstaticos.confirmado = false;
            sala = new DbSala(ObjetosEstaticos.sala.getNombre());
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("El sistema comunica: ");
            alerta.setHeaderText("");

            if (sala.insert()) {
                alerta.setContentText("Datos insertados correctamente");
                alerta.show();
            } else {
                alerta.setContentText("Hubo un error al insertar los datos");
                alerta.show();
            }

            tablaSalas.getItems().add(sala);
            registrosBase.add(sala);
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        DbSala salaSeleccionadaEliminar = tablaSalas.getSelectionModel().getSelectedItem();
        if (null != salaSeleccionadaEliminar) {
            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Aviso de borrado");
            confirmar.setHeaderText("");
            confirmar.setContentText("¿Estas seguro de eliminar la sala '" + salaSeleccionadaEliminar.getNombre() + "'?");

            if (confirmar.showAndWait().get() == ButtonType.OK) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                //tablaFuncionarios.getItems().remove(salaSeleccionadaEliminar);
                registrosBase.remove(salaSeleccionadaEliminar);
                tablaSalas.setItems(FXCollections.observableArrayList(registrosBase));
                if (salaSeleccionadaEliminar.delete()) {
                    alerta.setContentText("La sala '" + salaSeleccionadaEliminar.getNombre() + "' fue eliminada de la base de datos satisfactoriamente");
                } else {
                    alerta.setContentText("La sala '" + salaSeleccionadaEliminar.getNombre() + "' no pudo ser eliminada de la base de datos");
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
        DbSala salaSeleccionadaModificar = tablaSalas.getSelectionModel().getSelectedItem();

        if (null != salaSeleccionadaModificar) {
            ObjetosEstaticos.reloadSala = salaSeleccionadaModificar;
            DbSala aux = ObjetosEstaticos.sala;
            ObjetosEstaticos.sala = salaSeleccionadaModificar;

            abrirFXML("sala.fxml", "Modificar sala");
            ObjetosEstaticos.reloadSala = null;
            ObjetosEstaticos.sala = aux;
            tablaSalas.refresh();
            if (ObjetosEstaticos.confirmado == true) {
                ObjetosEstaticos.confirmado = false;
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                if (salaSeleccionadaModificar.update()) {
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
