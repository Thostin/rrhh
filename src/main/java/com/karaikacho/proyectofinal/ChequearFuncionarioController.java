/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import InfoTrespassing.ObjetosEstaticos;
import clases.DbFuncionario;
import clases.FuncionarioChecker;
import clases.FuncionarioChecker.Par;
import clases.FuncionarioChecker.ParDateTime;
import static clases.FuncionarioChecker.getRegistros;
import clases.RegistroFuncionario;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class ChequearFuncionarioController extends OpensFXML implements Initializable {

    DbFuncionario funcionarioSeleccionado = null;
    @FXML
    private Button btnChequearFuncionario;
    @FXML
    private TextField txtFuncionario;
    @FXML
    private Button btnRevisar;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private Pane inner_pane;
    @FXML
    private Pane most_inner_pane;
    @FXML
    private Label errorLabel;
    @FXML
    private Button btnVerRegistros;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void mostrarError(String txt) {
        if (errorLabel == null) {
            return;
        }
        errorLabel.setText(txt);
        errorLabel.setVisible(true);
    }

    public void limpiarError() {
        if (errorLabel == null) {
            return;
        }
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    @FXML
    private void chequear(ActionEvent event) {
        if (null == funcionarioSeleccionado) {
            mostrarError("Por favor, seleccione un funcionario");
            return;
        }

        if (null == fechaInicio.getValue() || null == fechaFin.getValue()) {
            mostrarError("Ninguna de las fechas puede ser nula");
            return;
        }

        if (fechaInicio.getValue().isAfter(fechaFin.getValue())) {
            mostrarError("La fecha de inicio no puede ser despues de la fecha final");
            return;
        }

        FuncionarioChecker.checkHorarioFuncionario(funcionarioSeleccionado, fechaInicio.getValue(), fechaFin.getValue());
    }

    @FXML
    private void buscarFuncionario(ActionEvent event) {
        abrirFXML("buscarFuncionarios.fxml", "Buscar Funcionario", (Stage) ((Node) event.getSource()).getScene().getWindow());
        funcionarioSeleccionado = ObjetosEstaticos.funcionarioSeleccion;
        txtFuncionario.setText(ObjetosEstaticos.funcionarioSeleccion.getNombres() + " " + ObjetosEstaticos.funcionarioSeleccion.getApellidos());
    }

    private String minutoAString(int minuto) {
        minuto %= 1440;
        String res = "";
        if (minuto / 60 < 10) {
            res += "0";
        }
        res += minuto / 60 + ":";
        minuto %= 60;
        if (minuto < 10) {
            res += "0";
        }
        res += minuto;
        return res;
    }

    @FXML
    private void verRegistros(ActionEvent event) {
        if (null == funcionarioSeleccionado) {
            mostrarError("Por favor, seleccione un funcionario");
            return;
        }

        if (null == fechaInicio.getValue() || null == fechaFin.getValue()) {
            mostrarError("Ninguna de las fechas puede ser nula");
            return;
        }

        if (fechaInicio.getValue().isAfter(fechaFin.getValue())) {
            mostrarError("La fecha de inicio no puede ser despues de la fecha final");
            return;
        }

        ArrayList<ParDateTime> registros = FuncionarioChecker.getRegistros(funcionarioSeleccionado.getId(), fechaInicio.getValue(), fechaFin.getValue());
        ArrayList<RegistroFuncionario> registroReporte = new ArrayList();
        ArrayList<Par> horario = FuncionarioChecker.getHorario(funcionarioSeleccionado.getId());

        LocalDateTime auxInicio = null;
        LocalDateTime auxFin = null;
        LocalDate iterar = fechaInicio.getValue();

        /* Generar todos las filas que corresponden a
         donde debe existir un registro, y despues cargarle el registro 
        correspondiente si es que existe
         */
        Par primerHorario = null;
        Par ultimoHorario = null;

        ParDateTime primeraMarca = null;
        ParDateTime ultimaMarca = null;
        int diaAux;

        while (!iterar.isAfter(fechaFin.getValue())) {
            diaAux = iterar.getDayOfWeek().getValue();
            /* Se encuentra una fecha que corresponde a una fila 
            (porque existe un horario en ese dia) */
            for (Par intervaloHorario : horario) {
                if (primerHorario == null) {
                    if (diaAux == 1 + intervaloHorario.inicio() / 1440) {
                        primerHorario = ultimoHorario = intervaloHorario;
                    }
                } else {
                    if (ultimoHorario.inicio() / 1440 == intervaloHorario.inicio() / 1440) {
                        ultimoHorario = intervaloHorario;
                    } else {
                        /* Si se llega aca es que horaEntrada/SalidaAux tienen
                        sus valores correspondientes (existe horario en esa fecha) */
 /* Ahora hay que conseguir las marcas de entrada y salida,
                           si es que existen */

                        for (ParDateTime marca : registros) {
                            /* Buscar marca entrada de la primera marca y 
                            marca salida de la ultima marca */
 /* Misma estructura */
                            if (null == primeraMarca) {
                                if (iterar.equals(marca.inicio().toLocalDate())) {
                                    primeraMarca = ultimaMarca = marca;
                                }
                            } else {
                                if (ultimaMarca.inicio().toLocalDate().equals(marca.inicio().toLocalDate())) {
                                    ultimaMarca = marca;
                                } else {
                                    /* Aca por fin se tiene lo que se quiere: el
                                    registro final, si es que existe, de los horarios 
                                    de entrada, salida y marca de entrada y salida */
                                    break;
                                }
                            }
                        }

                        String marcaEntrada = "";
                        String marcaSalida = "";
                        if (null != primeraMarca && null != ultimaMarca) {
                            /* En teoria uno es null si y solo si el otro es null */
                            marcaEntrada = "" + minutoAString(60 * primeraMarca.inicio().getHour() + primeraMarca.inicio().getMinute());
                            marcaSalida = "" + minutoAString(60 * ultimaMarca.fin().getHour() + ultimaMarca.fin().getMinute());
                        }

                        registroReporte.add(new RegistroFuncionario(iterar, minutoAString(primerHorario.inicio()), minutoAString(ultimoHorario.fin()), marcaEntrada, marcaSalida, "TODO", "TODO", "TODO"));

                        primerHorario = ultimoHorario = null;
                        primeraMarca = ultimaMarca = null;
                    }
                }
            }

            iterar = iterar.plusDays(1);
        }

        // 1. Load compiled report
        JasperReport jasperReport;

        // 2. Data
        // List<Person> people = List.of(new Person("Alice", 30), new Person("Bob", 40));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registroReporte);

        // 3. Parameters (optional)
        Map<String, Object> params = new HashMap<>();
        params.put("ds", dataSource);

        // 4. Fill
        JasperPrint jasperPrint;
        try {
            jasperReport = (JasperReport) JRLoader.loadObjectFromFile("/home/thotstin/Code/JAVA/RRHH/ProyectoFinal/src/main/java/com/karaikacho/proyectofinal/reportes/reporteAsistencias.jasper");
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
        } catch (JRException ex) {
            System.getLogger(FuncionarioChecker.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return;
        }

        // 5. Export to PDF
        // JasperExportManager.exportReportToPdfFile(jasperPrint, "output.pdf");
        // Or show it
        JasperViewer.viewReport(jasperPrint, false);

    }

}
