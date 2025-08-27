/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbEspecialidad;
import clases.DbOcupacion;
import clases.DetalleFuncionario;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
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
public class DetalleFuncionarioController implements Initializable {

    ObservableList<DetalleFuncionario> registros = null;

    @FXML
    private Button btnVolver;
    @FXML
    private TableView<DetalleFuncionario> tablaHorarios;
    @FXML
    private TableColumn<String, DetalleFuncionario> columnaDia;
    @FXML
    private TableColumn<String, DetalleFuncionario> columnaHoraEntrada;
    @FXML
    private TableColumn<String, DetalleFuncionario> columnaHoraSalida;
    @FXML
    private TableColumn<String, DetalleFuncionario> columnaSala;
    @FXML
    private TableColumn<String, DetalleFuncionario> columnaEsCompensatorio;
    @FXML
    private Label txtNombre;
    @FXML
    private Button btnAnadir;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registros = FXCollections.observableArrayList(DetalleFuncionario.readAll(ObjetosEstaticos.idFuncionario));
        columnaDia.setCellValueFactory(new PropertyValueFactory<>("dia"));
        columnaHoraEntrada.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        columnaHoraSalida.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        columnaSala.setCellValueFactory(new PropertyValueFactory<>("sala"));
        columnaEsCompensatorio.setCellValueFactory(new PropertyValueFactory<>("esCompensado"));
        tablaHorarios.setItems(registros);
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
    private void volver(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
    }

    @FXML
    private void anadirHorario(ActionEvent event) {
        ObjetosEstaticos.tieneFuncionario = true;
        abrirFXML("cargarHorario.fxml", "Cargar horario de " + ObjetosEstaticos.funcionarioCargarHorario.getNombres());

        tablaHorarios.setItems(FXCollections.observableArrayList(DetalleFuncionario.readAll(ObjetosEstaticos.idFuncionario)));
    }

    @FXML
    private void editar(ActionEvent event) {
        DetalleFuncionario ocupacionEditar = tablaHorarios.getSelectionModel().getSelectedItem();
        if (null == ocupacionEditar) {
            return;
        }

        ObjetosEstaticos.seModificaHorario = true;
        ObjetosEstaticos.ocupacionModificar = ocupacionEditar.getOcupacion();
        ObjetosEstaticos.detalleOcupacionModificar = ocupacionEditar;

        System.out.println("ID DE LA OCUPACION ANTES DE PASAR: " + ObjetosEstaticos.detalleOcupacionModificar.getOcupacion().getId());

        abrirFXML("cargarHorario.fxml", "Editar Horario de " + ObjetosEstaticos.funcionarioCargarHorario.getNombres());

        tablaHorarios.setItems(FXCollections.observableArrayList(DetalleFuncionario.readAll(ObjetosEstaticos.idFuncionario)));
    }

    @FXML
    private void eliminar(ActionEvent event) {
        DbOcupacion ocupacionSeleccionadaEliminar = tablaHorarios.getSelectionModel().getSelectedItem().getOcupacion();
        if (null != ocupacionSeleccionadaEliminar) {
            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Aviso de borrado");
            confirmar.setHeaderText("");
            confirmar.setContentText("¿Estas seguro de eliminar esa ocupacion");

            if (confirmar.showAndWait().get() == ButtonType.OK) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                if (ocupacionSeleccionadaEliminar.delete()) {
                    alerta.setContentText("La ocupacion con id = " + ocupacionSeleccionadaEliminar.getId() + " se ha eliminado satisfactoriamente");
                    tablaHorarios.setItems(FXCollections.observableArrayList(DetalleFuncionario.readAll(ObjetosEstaticos.idFuncionario)));
                } else {
                    alerta.setContentText("La ocupacion con id = " + ocupacionSeleccionadaEliminar.getId() + " no se pudo eliminar de la base de datos");
                }

                alerta.show();
            }
        }
    }
}
