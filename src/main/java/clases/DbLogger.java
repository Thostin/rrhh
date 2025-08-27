/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thotstin
 */
public class DbLogger {
    /* Un clasico */
    private String base;
    private String host;
    private String usuario;
    private String contrasena;

    private Connection con;

    public Connection getCon() {
        String url = "jdbc:mysql://"  + host + "/" + base;
        try {
            con = DriverManager.getConnection(url, this.usuario, this.contrasena);
            System.out.println("Conexion realizada con exito");
        } catch (SQLException ex) {
            Logger.getLogger(DbLogger.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al realizar la conexion");
        }
        
        return con;
    }
    
    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public DbLogger(String base, String host, String usuario, String contrasena) {
        this.base = base;
        this.host = host;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public DbLogger() {
        this.base = "horariosrrhh";
        this.host = "localhost";
        this.usuario = "root";
        this.contrasena = "";
    }
}
