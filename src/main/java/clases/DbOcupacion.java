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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thotstin
 */
public class DbOcupacion implements Sentencias {

    private int id = -1;

    /* Se guarda en minutos */
    private int horaInicio;
    private int horaFin;
    /*                      */

    private int idFuncionario;
    private int idSala;
    private int isCompensatorio;

    public DbOcupacion(int horaInicio, int horaFin, int idFuncionario, int idSala, int isCompensatorio) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.idFuncionario = idFuncionario;
        this.idSala = idSala;
        this.isCompensatorio = isCompensatorio;
    }

    public DbOcupacion(int id, int horaInicio, int horaFin, int idFuncionario, int idSala, int isCompensatorio) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.idFuncionario = idFuncionario;
        this.idSala = idSala;
        this.isCompensatorio = isCompensatorio;
    }

    @Override
    public boolean insert() {
        if (-1 != id) {
            System.out.println("Aparentemente este objeto pertenece a un registro en la base de datos, por lo que"
                    + "no puede ser insertado");
            return false;
        }

        String sql = "INSERT INTO Ocupacion (horaInicio, horaFin, idFuncionario, idSala, compensatorio) VALUES (?, ?, ?, ?, ?);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //stm.setInt(1, this.id);
            stm.setInt(1, this.horaInicio);
            stm.setInt(2, this.horaFin);
            stm.setInt(3, this.idFuncionario);
            stm.setInt(4, this.idSala);
            stm.setInt(5, this.isCompensatorio);
            stm.executeUpdate();

            ResultSet clave = stm.getGeneratedKeys();
            clave.next();
            id = clave.getInt(1);
            System.out.println("La ocupacion se anadio con id: " + id);
        } catch (SQLException ex) {
            Logger.getLogger(DbOcupacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    @Override
    public ArrayList read() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean update() {
        if (-1 == id) {
            System.out.println("Aparentemente este objeto no pertenece a un registro en la base de datos, por lo que"
                    + "no puede ser actualizado");
            return false;
        }

        String sql = "UPDATE Ocupacion SET horaInicio=?, horaFin=?, idFuncionario=?, idSala=?, compensatorio=? WHERE id=?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, this.horaInicio);
            stm.setInt(2, this.horaFin);
            stm.setInt(3, this.idFuncionario);
            stm.setInt(4, this.idSala);
            stm.setInt(5, this.isCompensatorio);
            stm.setInt(6, this.id);
            stm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DbOcupacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    @Override
    public boolean delete() {
        if (-1 == id) {
            System.out.println("Aparentemente este objeto no pertenece a un registro en la base de datos, por lo que"
                    + "no puede ser eliminado");
            return false;
        }

        String sql = "DELETE FROM Ocupacion WHERE id=" + id;
        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DbOcupacion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    public int getId() {
        return id;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public int getIdSala() {
        return idSala;
    }

    public int getIsCompensatorio() {
        return isCompensatorio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public void setIsCompensatorio(int isCompensatorio) {
        this.isCompensatorio = isCompensatorio;
    }

}
