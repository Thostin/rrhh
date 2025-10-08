/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.karaikacho.proyectofinal;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;

import java.io.*;

/**
 * FXML Controller class
 *
 * @author thotstin
 */
public class GeneralController extends OpensFXML implements Initializable {

    @FXML
    private AnchorPane side_ankerpane;
    @FXML
    private Button btnSalas;
    @FXML
    private Button btnFuncionarios;
    private Button btnEspecialidades;
    private Button btnCursos;
    @FXML
    private Button btnMarcar;
    @FXML
    private Button btnChequear;
    @FXML
    private Pane inner_pane;
    @FXML
    private Pane most_inner_pane;
    @FXML
    private Button btnAyuda;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void funcionarios(ActionEvent event) {
        btnFuncionarios.setDisable(true);
        abrirFXML("acceso.fxml", "Página de Funcionarios", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnFuncionarios.setDisable(false);
    }

    @FXML
    private void salas(ActionEvent event) {
        btnSalas.setDisable(true);
        abrirFXML("salas.fxml", "Salas", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnSalas.setDisable(false);
    }

    @FXML
    private void especialidades(ActionEvent event) {
        btnEspecialidades.setDisable(true);
        abrirFXML("especialidades.fxml", "Especialidades", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnEspecialidades.setDisable(false);
    }

    @FXML
    private void cursos(ActionEvent event) {
        btnCursos.setDisable(true);
        abrirFXML("cursos.fxml", "Cursos", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnCursos.setDisable(false);
    }

    @FXML
    private void marcar(ActionEvent event) {
        btnMarcar.setDisable(true);
        abrirFXML("marcar.fxml", "Marcar", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnMarcar.setDisable(false);
    }

    @FXML
    private void chequear(ActionEvent event) {
        btnChequear.setDisable(true);
        abrirFXML("chequearFuncionario.fxml", "Chequear", (Stage) ((Node) event.getSource()).getScene().getWindow());
        btnChequear.setDisable(false);
    }

    @FXML
    private void ayuda(ActionEvent event) {
        File tempFile;
        try (InputStream in = GeneralController.class.getResourceAsStream("/documentacion/documentacion.pdf")) {
            if (in == null) {
                System.out.println("Por lo visto se perdio la documentacion");
                return;
            }

            // Create a temporary file
            tempFile = File.createTempFile("DocumentacionRRHH", ".pdf");
            tempFile.deleteOnExit();

            // Copy resource to the temp file
            try (OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            if (!tempFile.exists() || !tempFile.canRead()) {
                System.err.println("File not accessible!");
                return;
            }

            String filePath = tempFile.getAbsolutePath();
            try {
                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder pb;

                if (os.contains("win")) {
                    pb = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", filePath);
                } else if (os.contains("mac")) {
                    pb = new ProcessBuilder("open", filePath);
                } else { // Linux and others
                    pb = new ProcessBuilder("xdg-open", filePath);
                }

                pb.inheritIO().start(); // won’t block

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        /*
        try {6
            new ProcessBuilder("xdg-open", "/home/thotstin/Code/JAVA/RRHH/ProyectoFinal/Documentation/out/documentacion.pdf").start();
        } catch (IOException ex) {
            System.getLogger(GeneralController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }*/
    }
    
    @FXML
    private void cargarHuella(ActionEvent event) {
        abrirFXML("cargarHuella.fxml", "Cargar Huella", (Stage) ((Node) event.getSource()).getScene().getWindow());
    }
}
