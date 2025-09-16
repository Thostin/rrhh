/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.ConnectionPool;
import clases.DbFuncionario;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class MarcarController extends OpensFXML implements Initializable {

    @FXML
    private TextField txtFuncionario;
    @FXML
    private Button btnBuscarFuncionario;
    @FXML
    private TextField txtHora;
    @FXML
    private TextField txtReemplazante;
    @FXML
    private Button btnMarcar;
    @FXML
    private DatePicker datePickerFecha;

    private DbFuncionario funcionarioSeleccionado = null;
    private DbFuncionario funcionarioReemplazante = null;
    @FXML
    private Button btnBuscarReemplazante;
    @FXML
    private CheckBox esReemplazado;
    @FXML
    private Pane paneReemplazante;
    @FXML
    private Pane inner_pane;
    @FXML
    private Pane most_inner_pane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /// ! ChatGPT
        Pattern strictHHmm = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

        txtHora.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // user clicked away
                String text = txtHora.getText();
                if (!strictHHmm.matcher(text).matches()) {
                    txtHora.clear(); // or revert to last valid value
                }
            }
        });
    }

    @FXML
    private void buscarFuncionario(ActionEvent event) {
        abrirFXML("buscarFuncionarios.fxml", "Buscar Funcionario", (Stage) ((Node) event.getSource()).getScene().getWindow());
        if (null != ObjetosEstaticos.funcionarioSeleccion) {
            txtFuncionario.setText(ObjetosEstaticos.funcionarioSeleccion.getNombres());
        }

        funcionarioSeleccionado = ObjetosEstaticos.funcionarioSeleccion;
        ObjetosEstaticos.funcionarioSeleccion = null;
    }

    @FXML
    private void buscarReemplazante(ActionEvent event) {
        abrirFXML("buscarFuncionarios.fxml", "Buscar Reemplazante", (Stage) ((Node) event.getSource()).getScene().getWindow());
        if (null != ObjetosEstaticos.funcionarioSeleccion) {
            txtReemplazante.setText(ObjetosEstaticos.funcionarioSeleccion.getNombres());
        }

        funcionarioReemplazante = ObjetosEstaticos.funcionarioSeleccion;
        ObjetosEstaticos.funcionarioSeleccion = null;
    }

    @FXML
    private void marcar(ActionEvent event) {
        if (funcionarioSeleccionado == null) {
            System.out.println("no anda tu programa de mierda");
            return;
        }

        System.out.println("lesufhin3iebvuw8e");
        DateTimeFormatter hhmm = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaAux = LocalTime.parse(txtHora.getText(), hhmm);

        LocalDateTime hora = LocalDateTime.of(datePickerFecha.getValue(), horaAux);

        // Variable para guardar la clave generada
        int id;
        String sql = "INSERT INTO REGISTROS (hora, idFuncionario) VALUES (?, ?);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //stm.setInt(1, this.id);
            stm.setObject(1, hora);
            stm.setInt(2, funcionarioSeleccionado.getId());
            stm.executeUpdate();

            ResultSet clave = stm.getGeneratedKeys();
            clave.next();
            id = clave.getInt(1);
            System.out.println("El registro se aniadio con id: " + id);
        } catch (SQLException ex) {
            Logger.getLogger(MarcarController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        if (!esReemplazado.isSelected() || null == funcionarioReemplazante) {
            return;
        }

        sql = "INSERT INTO Reemplazantes (idREGISTROS, idFuncionario) VALUES (?, ?);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);
            //stm.setInt(1, this.id);
            stm.setInt(1, id);
            stm.setInt(2, funcionarioReemplazante.getId());
            stm.executeUpdate();

            System.out.println("El registro se aniadio correctamente");
        } catch (SQLException ex) {
            Logger.getLogger(MarcarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void invertirMostrarReemplazante(ActionEvent event) {
        paneReemplazante.setVisible(!paneReemplazante.isVisible());
    }
}
