/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbOcupacion;
import clases.DbSala;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class CargarHorarioController implements Initializable {

    @FXML
    private TextField txtHoraInicio;
    @FXML
    private TextField txtHoraFin;
    @FXML
    private TextField txtFuncionario;
    @FXML
    private Button btnBuscarFuncionario;
    @FXML
    private TextField txtSala;
    @FXML
    private Button btnBuscarSala;
    @FXML
    private ComboBox<String> cmbEsCompensatorio;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<String> cmbDia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (ObjetosEstaticos.tieneFuncionario == true) {

            txtFuncionario.setText(ObjetosEstaticos.funcionarioCargarHorario.getNombres() + " " + ObjetosEstaticos.funcionarioCargarHorario.getApellidos());
            btnBuscarFuncionario.setDisable(true);

            /* MUY IMPORTANTE */
            ObjetosEstaticos.funcionarioSeleccion = ObjetosEstaticos.funcionarioCargarHorario;
        }
        cmbEsCompensatorio.getItems().addAll("Si", "No");
        cmbDia.getItems().addAll("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");

        cmbEsCompensatorio.getSelectionModel().select("No");
        cmbDia.getSelectionModel().select("Lunes");

        if (ObjetosEstaticos.seModificaHorario == true) {
            txtHoraInicio.setText(ObjetosEstaticos.detalleOcupacionModificar.getHoraInicio());
            txtHoraFin.setText(ObjetosEstaticos.detalleOcupacionModificar.getHoraFin());
            btnBuscarFuncionario.setDisable(true);
            txtFuncionario.setText(ObjetosEstaticos.funcionarioCargarHorario.getNombres());
            txtSala.setText(ObjetosEstaticos.detalleOcupacionModificar.getSala());

            if (ObjetosEstaticos.detalleOcupacionModificar.getEsCompensado().equals("Si")) {
                cmbEsCompensatorio.getSelectionModel().select("Si");
            }

            // Simular como que ya se selecciono
            ObjetosEstaticos.salaSeleccion = new DbSala(ObjetosEstaticos.detalleOcupacionModificar.getOcupacion().getIdSala(),
                    ObjetosEstaticos.detalleOcupacionModificar.getSala());
            ObjetosEstaticos.funcionarioSeleccion = ObjetosEstaticos.funcionarioCargarHorario;
        }

        /// ! ChatGPT
        Pattern strictHHmm = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

        txtHoraInicio.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // user clicked away
                String text = txtHoraInicio.getText();
                if (!strictHHmm.matcher(text).matches()) {
                    txtHoraInicio.clear(); // or revert to last valid value
                }
            }
        });

        txtHoraFin.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) { // user clicked away
                String text = txtHoraFin.getText();
                if (!strictHHmm.matcher(text).matches()) {
                    txtHoraFin.clear(); // or revert to last valid value
                }
            }
        });
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
    private void buscarFuncionario(ActionEvent event) {
        abrirFXML("buscarFuncionarios.fxml", "Seleccionar Funcionario");
        txtFuncionario.setText(ObjetosEstaticos.funcionarioSeleccion.getNombres()
                + " " + ObjetosEstaticos.funcionarioSeleccion.getApellidos());
    }

    @FXML
    private void buscarSala(ActionEvent event) {
        abrirFXML("buscarSalas.fxml", "Seleccionar Funcionario");
        txtSala.setText(ObjetosEstaticos.salaSeleccion.getNombre());
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void confirmar(ActionEvent event) {
        DateTimeFormatter hhmm = DateTimeFormatter.ofPattern("HH:mm");
        int dia = 0;

        // Aca Java convierte a .equals, no comete el error de comparar con '=='
        switch (cmbDia.getSelectionModel().getSelectedItem()) {
            case "Lunes" ->
                dia = 0;
            case "Martes" ->
                dia = 1;
            case "Miercoles" ->
                dia = 2;
            case "Jueves" ->
                dia = 3;
            case "Viernes" ->
                dia = 4;
            case "Sabado" ->
                dia = 5;
            case "Domingo" ->
                dia = 6;
        }

        System.out.println("VALOR DE dia = " + dia);

        LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText(), hhmm);
        LocalTime horaFin = LocalTime.parse(txtHoraFin.getText(), hhmm);

        int esComp = "Si".equals(cmbEsCompensatorio.getSelectionModel().getSelectedItem()) ? 1 : 0;

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("El sistema comunica: ");
        alerta.setHeaderText("");
        DbOcupacion ocupacion;
        boolean resultadoErr;
        if (ObjetosEstaticos.seModificaHorario == true) {
            ocupacion
                    = new DbOcupacion(
                            ObjetosEstaticos.detalleOcupacionModificar.getOcupacion().getId(),
                            horaInicio.getHour() * 60 + horaInicio.getMinute() + 1440 * dia,
                            horaFin.getHour() * 60 + horaInicio.getMinute() + 1440 * dia,
                            ObjetosEstaticos.funcionarioSeleccion.getId(),
                            ObjetosEstaticos.salaSeleccion.getId(),
                            esComp);

            System.out.println("ID DE LA OCUPACION A MODIFICAR: " + ObjetosEstaticos.detalleOcupacionModificar.getOcupacion().getId());

            ocupacion.setId(ObjetosEstaticos.ocupacionModificar.getId());
            resultadoErr = ocupacion.update();
        } else {
            ocupacion
                    = new DbOcupacion(
                            horaInicio.getHour() * 60 + horaInicio.getMinute() + 1440 * dia,
                            horaFin.getHour() * 60 + horaInicio.getMinute() + 1440 * dia,
                            ObjetosEstaticos.funcionarioSeleccion.getId(),
                            ObjetosEstaticos.salaSeleccion.getId(),
                            esComp
                    );

            resultadoErr = ocupacion.insert();
        }

        if (resultadoErr) {
            alerta.setContentText("Datos insertados correctamente");
            txtFuncionario.clear();
            txtHoraInicio.clear();
            txtHoraFin.clear();
            txtSala.clear();
            alerta.show();
        } else {
            alerta.setContentText("Hubo un error al insertar los datos");
            alerta.show();
        }

        if (ObjetosEstaticos.tieneFuncionario == true || ObjetosEstaticos.seModificaHorario == true) {
            ObjetosEstaticos.tieneFuncionario = false;
            ObjetosEstaticos.seModificaHorario = false;

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}
