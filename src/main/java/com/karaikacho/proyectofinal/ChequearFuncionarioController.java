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
import static clases.FuncionarioChecker.toMinute;
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

    private boolean seSolapa(Par horario, ParDateTime marca) {
        int inicioMarca = toMinute(marca.inicio());
        int finMarca = toMinute(marca.fin());

        if (inicioMarca < horario.fin() && finMarca > horario.inicio()) {
            return true;
        }

        return false;
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
        ArrayList<RegistroFuncionario> registroReporte = new ArrayList<>();
        ArrayList<Par> horario = FuncionarioChecker.getHorario(funcionarioSeleccionado.getId());

        LocalDateTime auxInicio = null;
        LocalDateTime auxFin = null;
        // LocalDate iterar = fechaInicio.getValue();

        /* Generar todos las filas que corresponden a
         donde debe existir un registro, y despues cargarle el registro 
        correspondiente si es que existe
         */
        Par primerHorario = null;
        Par ultimoHorario = null;

        ParDateTime primeraMarca = null;
        ParDateTime ultimaMarca = null;
        int diaAux;
        int auxX;

        String marcaEntrada = "";
        String marcaSalida = "";
        String strEsLlegadaTardia = "";
        String strEsSalidaAnticipada = "";

        for (LocalDate iterar = fechaInicio.getValue(); !iterar.isAfter(fechaFin.getValue()); iterar = iterar.plusDays(1)) {
            // INTENTO DE DESCRIPCION DE LOGICA
            /*
            Revisa fecha por fecha, si existe un horario que corresponde a esa fecha, entonces 
            automaticamente se genera una fila en el reporte. Si existe una marca de entrada o salida
            con esa marca de ENTRADA, pero la salida aun sin definir. Si la salida es durante un horario, 
            entonces se anade atutomaticamente esa fila y se cuenta como salida anticipada. 
            
            Si no, entonces se busca el siguiente horario de entrada y asi sucesivamente hasta que 
            sea el ultimo, y ahi se pasa hasta la siguiente fecha.
             */

 /*
            Algoritmo: Para cada fecha, ver horario por horario (no es necesario optimizar, hay muy pocos horarios de un 
            solo funcionario, a lo sumo 15) y si se encuentra uno en esa fecha, entonces hay que conseguir eso y 
            volver a iterar desde ahi, y aplicar lo que estar descrito ahi: 
            basicamente conseguir el conjunto de horarios que pertenecen a esa fecha. 
            Eso voy a hacer con una busqueda lineal en los horarios indice por indice y despues iterar en ese intervalo
            Optimizar es opcional.
             */
            int primer = -1;
            int final_ = -1;
            for (int i = 0; i < horario.size(); ++i) {
                if (primer == -1 && 1 + horario.get(i).inicio() / 1440 == iterar.getDayOfWeek().getValue()) {
                    primer = i;
                } else if (-1 != primer && 1 + horario.get(i).inicio() / 1440 != iterar.getDayOfWeek().getValue()) {
                    final_ = i - 1;
                    break;
                }
            }
            if (-1 == primer) {
                break;
            }

            //Encontrar las marcas de ese dia
            // TIENE QUE MOSTRAR EL HORARIO INCLUSO SI NO VINO
            int primerMarca = -1;
            int finalMarca = -1;
            for (int i = 0; i < registros.size(); ++i) {
                if (primerMarca == -1 && 1 + toMinute(registros.get(i).inicio()) / 1440 == iterar.getDayOfWeek().getValue()) {
                    primerMarca = finalMarca = i;
                } else if (-1 != primerMarca && 1 + toMinute(registros.get(i).inicio()) / 1440 != iterar.getDayOfWeek().getValue()) {
                    finalMarca = i - 1; // Clave el -1
                    break;
                }
            }

//registroReporte.add(new RegistroFuncionario(iterar, minutoAString(primerHorario.inicio()), minutoAString(ultimoHorario.fin()), marcaEntrada, marcaSalida, "TODO", "TODO", "TODO"));
            // Iterar en los horarios de ese dia
            // NOTA: La idea es mostrar TODAS las marcas
            // Mostrar los intervalos de horario solos 
            // solamente si no hay horario que cubra parte de ellos.
            // Ordenar el resultado de alguna manera
            for (int j = primerMarca; j <= finalMarca; ++j) {

                // Ver si es Llegada tardia o Salida anticipada
                for (int i = primer; i <= final_; ++i) {
                    if (horario.get(i).inicio() < toMinute(registros.get(j).inicio()) && horario.get(i).fin() > toMinute(registros.get(j).inicio())) {
                        strEsLlegadaTardia = "Y";
                    }
                    if (horario.get(i).inicio() < toMinute(registros.get(j).fin()) && horario.get(i).fin() > toMinute(registros.get(j).fin())) {
                        strEsSalidaAnticipada = "Y";
                    }
                }

                // Consultar todos los horarios que se solapan con estos
                // y mostrar el horario de entrada del primero y
                // el horario de salida del ultimo
                for (int i = primer; i <= final_; ++i) {
                    if (seSolapa(horario.get(i), registros.get(j))) {
                        if (primerHorario == null) {
                            primerHorario = ultimoHorario = horario.get(i);
                        } else {
                            ultimoHorario = horario.get(i);
                        }
                    }
                }

                if (null != primerHorario) {
                    registroReporte.add(new RegistroFuncionario(iterar, minutoAString(primerHorario.inicio()), minutoAString(ultimoHorario.fin()), minutoAString(toMinute(registros.get(j).inicio()) % 86400), minutoAString(toMinute(registros.get(j).fin()) % 86400), strEsLlegadaTardia, strEsSalidaAnticipada, "TODO"));
                } else {
                    registroReporte.add(new RegistroFuncionario(iterar, minutoAString(primerHorario.inicio()), minutoAString(ultimoHorario.fin()), minutoAString(toMinute(registros.get(j).inicio()) % 86400), minutoAString(toMinute(registros.get(j).fin()) % 86400), strEsLlegadaTardia, strEsSalidaAnticipada, "TODO"));
                }
            }

            boolean haySolapo = false;
            for (int i = primer; i <= final_; ++i) {
                for (int j = primerMarca; j <= finalMarca; ++j) {
                    if (seSolapa(horario.get(i), registros.get(j))) {
                        haySolapo = true;
                    }
                }
                if (!haySolapo) {
                    registroReporte.add(new RegistroFuncionario(iterar, minutoAString(horario.get(i).inicio()), minutoAString(horario.get(i).fin()), "", "", "", "", "Y"));
                }
                haySolapo = false;
            }
        }

        // 1. Load compiled report
        JasperReport jasperReport;

        // 2. Data
        // List<Person> people = List.of(new Person("Alice", 30), new Person("Bob", 40));
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registroReporte);

        // 3. Parameters (optional)
        Map<String, Object> params = new HashMap<>();

        params.put(
                "ds", dataSource);

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
        JasperViewer.viewReport(jasperPrint,
                false);

    }

}
