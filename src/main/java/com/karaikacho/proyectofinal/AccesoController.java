/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbFuncionario;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class AccesoController implements Initializable {

    ArrayList<DbFuncionario> registrosBase = null;
    ObservableList<DbFuncionario> registros = null;
    ObservableList<DbFuncionario> registrosFiltrados = null;

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
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnVerHorario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println(getClass().getResource(""));
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

    public void abrirFXML(String direccion, String titulo) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(direccion));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));

            // Dos lineas de chatgpt:
            stage.sizeToScene(); //  this will set the stage size to match the FXML
            stage.setResizable(false); // optional: prevents resizing

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void nuevo(ActionEvent event) {
        abrirFXML("funcionario.fxml", "Anadir Funcionario");
        DbFuncionario funcionario;
        if (ObjetosEstaticos.confirmado == true) {
            ObjetosEstaticos.confirmado = false;
            funcionario = new DbFuncionario(ObjetosEstaticos.funcionario.getId(), ObjetosEstaticos.funcionario.getNombres(), ObjetosEstaticos.funcionario.getApellidos(), ObjetosEstaticos.funcionario.getCi(), ObjetosEstaticos.funcionario.getLineaBaja(), ObjetosEstaticos.funcionario.getCelular());
            tablaFuncionarios.getItems().add(funcionario);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("El sistema comunica: ");
            alerta.setHeaderText("");

            if (funcionario.insert()) {
                alerta.setContentText("Datos insertados correctamente");
                alerta.show();
            } else {
                alerta.setContentText("Hubo un error al insertar los datos");
                alerta.show();
            }

            registrosBase.add(funcionario);
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        DbFuncionario funcionarioSeleccionadoModificar = tablaFuncionarios.getSelectionModel().getSelectedItem();

        if (null != funcionarioSeleccionadoModificar) {
            ObjetosEstaticos.reloadFuncionario = funcionarioSeleccionadoModificar;
            DbFuncionario aux = ObjetosEstaticos.funcionario;
            ObjetosEstaticos.funcionario = funcionarioSeleccionadoModificar;

            abrirFXML("funcionario.fxml", "Modificar funcionario");
            ObjetosEstaticos.reloadFuncionario = null;
            ObjetosEstaticos.funcionario = aux;
            tablaFuncionarios.refresh();
            if (ObjetosEstaticos.confirmado == true) {
                ObjetosEstaticos.confirmado = false;
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                if (funcionarioSeleccionadoModificar.update()) {
                    alerta.setContentText("Datos actualizados correctamente");
                    alerta.show();
                } else {
                    alerta.setContentText("Hubo un error al insertar los datos");
                    alerta.show();
                }
            }

        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        DbFuncionario funcionarioSeleccionadoEliminar = tablaFuncionarios.getSelectionModel().getSelectedItem();
        if (null != funcionarioSeleccionadoEliminar) {
            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Aviso de borrado");
            confirmar.setHeaderText("");
            confirmar.setContentText("Estas seguro de eliminar a '" + funcionarioSeleccionadoEliminar.getNombres() + "' " + "'" + funcionarioSeleccionadoEliminar.getApellidos() + "'?");

            if (confirmar.showAndWait().get() == ButtonType.OK) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica: ");
                alerta.setHeaderText("");

                //tablaFuncionarios.getItems().remove(funcionarioSeleccionadoEliminar);
                registrosBase.remove(funcionarioSeleccionadoEliminar);
                tablaFuncionarios.setItems(FXCollections.observableArrayList(registrosBase));
                if (funcionarioSeleccionadoEliminar.delete()) {
                    alerta.setContentText("'" + funcionarioSeleccionadoEliminar.getNombres() + "' " + "'" + funcionarioSeleccionadoEliminar.getApellidos() + "' fue eliminado de la base de datos satisfactoriamente");
                } else {
                    alerta.setContentText("'" + funcionarioSeleccionadoEliminar.getNombres() + "' " + "'" + funcionarioSeleccionadoEliminar.getApellidos() + "' no pudo ser eliminado de la base de datos");
                }

                alerta.show();
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void verHorario(ActionEvent event) {
        if (null != tablaFuncionarios.getSelectionModel().getSelectedItem()) {
            ObjetosEstaticos.funcionarioCargarHorario = tablaFuncionarios.getSelectionModel().getSelectedItem();
            ObjetosEstaticos.idFuncionario = tablaFuncionarios.getSelectionModel().getSelectedItem().getId();
            abrirFXML("detalleFuncionario.fxml", "Horario de "
                    + tablaFuncionarios.getSelectionModel().getSelectedItem().getNombres()
                    + " " + tablaFuncionarios.getSelectionModel().getSelectedItem().getApellidos());
        }
    }
}
