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
public class DbCurso implements Sentencias {

    private int id = -1;
    private String nombre;

    private DbEspecialidad especialidad = new DbEspecialidad();

    @Override
    public boolean insert() {
        if (-1 != id) {
            throw new IllegalArgumentException("Aparentemente este objeto pertenece a un registro en la base de datos, por lo que"
                    + "no puede ser insertado");
        }

        String sql = "INSERT INTO Curso (nombre, idEspecialidad) VALUES (?, ?);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //stm.setInt(1, this.id);
            stm.setString(1, this.nombre);
            stm.setInt(2, this.especialidad.getId());
            stm.executeUpdate();

            ResultSet clave = stm.getGeneratedKeys();
            clave.next();
            id = clave.getInt(1);
            System.out.println("El curso se aniadio con id: " + id);
        } catch (SQLException ex) {
            Logger.getLogger(DbFuncionario.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public DbCurso() {
    }

    public DbCurso(String nombre, int idEspecialidad) {
        this.id = -1;
        this.nombre = nombre;
        this.especialidad.read(idEspecialidad);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public DbCurso(int id, String nombre, int idEspecialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad.read(idEspecialidad);
    }

    @Override
    public ArrayList read() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static ArrayList<DbCurso> readAll() {
        ArrayList<DbCurso> cursos = new ArrayList();
        String sql = "SELECT * FROM Curso;";

        try (Connection con = ConnectionPool.getConnection(); Statement stm = con.createStatement();) {
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                int ID = resultado.getInt("id");
                String NOMBRE = resultado.getString("nombre");
                int ID_ESPECIALIDAD = resultado.getInt("idEspecialidad");

                cursos.add(new DbCurso(ID, NOMBRE, ID_ESPECIALIDAD));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbCurso.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        //Collections.sort(cursos, new SortbyRoll());
        return cursos;
    }

    public void read(int id) {
        if (-1 != this.id) {
            System.out.println("Al parecer este objeto esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser leido");
        }

        String sql = "SELECT * FROM Curso WHERE id=" + id;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet resultado = stm.executeQuery();
            if (resultado.next()) {
                this.id = id;
                this.nombre = resultado.getString("nombre");
                this.especialidad.read(resultado.getInt("idEspecialidad"));
            } else {
                System.out.println("Aparentemente no existe una curso con ese id = " + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbCurso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean update() {
        if (-1 == id) {
            System.out.println("Al parecer este objeto no esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser actualizado");
        }
        String sql = "UPDATE Curso SET nombre = ?, idEspecialidad = ? WHERE id = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);
            //stm.setInt(1, this.id);
            stm.setString(1, this.nombre);
            stm.setInt(2, this.especialidad.getId());
            stm.setInt(3, this.id);
            stm.executeUpdate();

            System.out.println("El Curso con id = " + id + " ahora tiene el nombre \"" + nombre + "\" e idEspecialidad = " + this.especialidad.getId());
        } catch (SQLException ex) {
            Logger.getLogger(DbCurso.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean delete() {
        if (-1 == id) {
            System.out.println("Al parecer este objeto no esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser borrado");
        }

        String sql = "DELETE FROM Curso WHERE id=" + id;
        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            System.out.println("El Curso con id = " + id + " ha sido eliminado con exito");
            //id = -1;
        } catch (SQLException ex) {
            Logger.getLogger(DbCurso.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DbEspecialidad getEspecialidad() {
        return especialidad;
    }
    
    public String getEspecialidadNombre(){
        return especialidad.getNombre();
    }

    public void setEspecialidad(DbEspecialidad especialidad) {
        this.especialidad = especialidad;
    }

}
