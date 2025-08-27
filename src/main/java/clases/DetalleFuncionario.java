/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thotstin
 */
public class DetalleFuncionario {

    private DbOcupacion ocupacion;
    private String dia = "";
    private String horaInicio = "";
    private String horaFin = "";
    String sala = "";
    private String esCompensado = "";

    private String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};

    public void loadOcupacion() {
        int horaInicio = ocupacion.getHoraInicio(), horaFin = ocupacion.getHoraFin();
        int dia;
        dia = horaInicio / 1440;
        this.dia = dias[dia];
        horaInicio -= dia * 1440;

        this.horaInicio = this.horaFin = "";

        if (horaInicio / 60 < 10) {
            this.horaInicio += "0";
        }
        this.horaInicio += horaInicio / 60;
        if (horaInicio % 60 < 10) {
            this.horaInicio += ":0";
        } else {
            this.horaInicio += ":";
        }
        this.horaInicio += horaInicio % 60;

        horaFin -= 1440 * dia;
        if (horaFin / 60 < 10) {
            this.horaFin += "0";
        }
        this.horaFin += horaFin / 60;
        if (horaFin % 60 < 10) {
            this.horaFin += ":0";
        } else {
            this.horaFin += ":";
        }
        this.horaFin += horaFin % 60;

        /* ES UNA CONSULTA SQL MUY SENCILLA, POR LO QUE ES EXTREMADAMENTE RAPIDA */
        String sql = "SELECT nombre FROM Sala WHERE id=" + ocupacion.getIdSala();
        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            if (resultado.next()) {
                this.sala = resultado.getString("nombre");
            } else {
                System.out.println("Aparentemente no existe una sala con ese id = " + ocupacion.getIdSala());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetalleFuncionario.class.getName()).log(Level.SEVERE, null, ex);
        }

        switch (ocupacion.getIsCompensatorio()) {
            case 0 ->
                this.esCompensado = "No";
            case 1 ->
                this.esCompensado = "Si";
        }
    }

    public DetalleFuncionario(DbOcupacion ocupacion) {
        this.ocupacion = ocupacion;
        System.out.println("ANTES DE CREAR EL DETALLE, ID_OCUPACION = " + ocupacion.getId());
        loadOcupacion();

        System.out.println("DESPUES DE CREAR EL DETALLE, ID_OCUPACION = " + this.ocupacion.getId());
    }

    public static ArrayList<DetalleFuncionario> readAll(int idFuncionario) {
        ArrayList<DetalleFuncionario> detalles = new ArrayList();

        DbOcupacion aux;
        String sql = "SELECT id, horaInicio, horaFin, idSala, compensatorio FROM Ocupacion WHERE idFuncionario=" + idFuncionario;
        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                aux = new DbOcupacion(
                        resultado.getInt("id"),
                        resultado.getInt("horaInicio"),
                        resultado.getInt("horaFin"),
                        idFuncionario,
                        resultado.getInt("idSala"),
                        resultado.getInt("compensatorio")
                );
                detalles.add(new DetalleFuncionario(aux));
                System.out.println("ID DE OCUPACION CARGADA: " + resultado.getInt("id"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DetalleFuncionario.class.getName()).log(Level.SEVERE, null, ex);
        }

        return detalles;
    }

    public DbOcupacion getOcupacion() {
        return ocupacion;
    }

    public String getDia() {
        return dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getSala() {
        return sala;
    }

    public String getEsCompensado() {
        return esCompensado;
    }

}
