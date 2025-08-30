/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class ModificarHorarioController extends OpensFXML implements Initializable {

    @FXML
    private TableView<FilaDato> tablaHorario;
    @FXML
    private TableColumn<FilaDato, String> columnaA;
    @FXML
    private TableColumn<FilaDato, String> columnaB;
    @FXML
    private TableColumn<FilaDato, String> columnaC;
    @FXML
    private TableColumn<FilaDato, String> columnaD;
    @FXML
    private TableColumn<FilaDato, String> columnaE;
    @FXML
    private CheckBox boxEsProfesor;
    @FXML
    private Button btnAplicar;
    @FXML
    private Button btnSalir;
    @FXML
    private TextArea txtSala;
    @FXML
    private Button btnBuscarSala;
    @FXML
    private TextArea txtCurso;
    @FXML
    private TextField txtMateria;
    @FXML
    private Button btnBuscarCurso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaA.setCellValueFactory(new PropertyValueFactory<>("a"));
        columnaB.setCellValueFactory(new PropertyValueFactory<>("b"));
        columnaC.setCellValueFactory(new PropertyValueFactory<>("c"));
        columnaD.setCellValueFactory(new PropertyValueFactory<>("a"));
        columnaE.setCellValueFactory(new PropertyValueFactory<>("b"));

        ObservableList<FilaDato> datos = FXCollections.observableArrayList(
                new FilaDato("", "", "", "", ""),
                new FilaDato("", "", "", "", ""),
                new FilaDato("", "", "", "", ""),
                new FilaDato("", "", "", "", ""),
                new FilaDato("", "", "", "", "")
        );

        tablaHorario.setItems(datos);
        tablaHorario.getSelectionModel().setCellSelectionEnabled(true);
        tablaHorario.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    private void marcaDesmarca(ActionEvent event) {
        if (txtMateria.isDisabled()) {
            txtMateria.setDisable(false);
            btnBuscarCurso.setDisable(false);
        } else {
            txtMateria.setDisable(true);
            btnBuscarCurso.setDisable(true);
            txtCurso.setText("");
            txtMateria.setText("");
            ObjetosEstaticos.salaSeleccion = null;
            
        }
    }

    @FXML
    private void aplicar(ActionEvent event) {
    }

    @FXML
    private void salir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void buscarSala(ActionEvent event) {
        abrirFXML("buscarSala.fxml", "Buscar Sala", (Stage) ((Node) event.getSource()).getScene().getWindow());
        txtSala.setText(ObjetosEstaticos.salaSeleccion.getNombre());
    }

    @FXML
    private void buscarCurso(ActionEvent event) {
        abrirFXML("buscarCursos.fxml", "Buscar Cursos", (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    public static class FilaDato {

        private final String a, b, c, d, e;

        public FilaDato(String a, String b, String c, String d, String e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }

        public String getC() {
            return c;
        }

        public String getD() {
            return d;
        }

        public String getE() {
            return e;
        }
    }

}
