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
public class DbEspecialidad implements Sentencias {

    private int id = -1;
    private String nombre;

    @Override
    public boolean insert() {
        if (-1 != id) {
            throw new IllegalArgumentException("Aparentemente este objeto pertenece a un registro en la base de datos, por lo que"
                    + "no puede ser insertado");
        }

        String sql = "INSERT INTO Especialidad (nombre) VALUES (?);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //stm.setInt(1, this.id);
            stm.setString(1, this.nombre);
            stm.executeUpdate();

            ResultSet clave = stm.getGeneratedKeys();
            clave.next();
            id = clave.getInt(1);
            System.out.println("La especialidad se aniadio con id: " + id);
        } catch (SQLException ex) {
            Logger.getLogger(DbFuncionario.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public DbEspecialidad() {
    }

    public DbEspecialidad(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public DbEspecialidad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public ArrayList read() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static ArrayList<DbEspecialidad> readAll() {
        ArrayList<DbEspecialidad> especialidades = new ArrayList();
        String sql = "SELECT * FROM Especialidad;";

        try (Connection con = ConnectionPool.getConnection(); Statement stm = con.createStatement();) {
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                int ID = resultado.getInt("id");
                String NOMBRE = resultado.getString("nombre");

                especialidades.add(new DbEspecialidad(ID, NOMBRE));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbEspecialidad.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Collections.sort(especialidades, new SortbyRoll());
        return especialidades;
    }

    public void read(int id) {
        if (-1 != this.id) {
            System.out.println("Al parecer este objeto esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser leido");
            return;
        }

        String sql = "SELECT * FROM Especialidad WHERE id=" + id;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet resultado = stm.executeQuery();
            if (resultado.next()) {
                this.id = id;
                this.nombre = resultado.getString("nombre");
            } else {
                System.out.println("Aparentemente no existe una especialidad con ese id = " + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbEspecialidad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean update() {
        if (-1 == id) {
            System.out.println("Al parecer este objeto no esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser actualizado");
        }
        String sql = "UPDATE Especialidad SET nombre = ? WHERE id = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);
            //stm.setInt(1, this.id);
            stm.setString(1, this.nombre);
            stm.setInt(2, this.id);
            stm.executeUpdate();

            System.out.println("La especialidad con id = " + id + " ahora tiene el nombre \"" + nombre + "\"");
        } catch (SQLException ex) {
            Logger.getLogger(DbEspecialidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean delete() {
        if (-1 == id) {
            System.out.println("Al parecer este objeto no esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser borrado");
        }
        String sql = "DELETE FROM Especialidad WHERE id=" + id;
        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            System.out.println("La sala con id = " + id + " ha sido eliminada con exito");
            //id = -1;
        } catch (SQLException ex) {
            Logger.getLogger(DbEspecialidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
