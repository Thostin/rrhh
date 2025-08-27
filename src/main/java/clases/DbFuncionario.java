/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/* La sugerencia de chatgpt es buenisima: */

 /* Basicamente puedo crear una clase estatica que tenga un objeto 
   que almacene conexiones para reutilizarlas. 

   La unica manera para que el pool sepa que ya se ha usado 
   una conexion es cuando llamas a .close(), ahi el pool intercepta esa 
   llamada sabe que esa conexion puede ser reutilizada
 */
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
public class DbFuncionario implements Sentencias {

    private int id = -1;
    private String nombres = "David";
    private String apellidos = "Alcaparra";
    private String ci = "";
    private String lineaBaja = "";
    private String celular = "";

    public DbFuncionario() {

    }

    public DbFuncionario(String nombres, String apellidos, String ci, String lineaBaja, String celular) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.ci = ci;
        this.lineaBaja = lineaBaja;
        this.celular = celular;
    }

    public DbFuncionario(int id, String nombres, String apellidos, String ci, String lineaBaja, String celular) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.ci = ci;
        this.lineaBaja = lineaBaja;
        this.celular = celular;
    }

    public int getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCi() {
        return ci;
    }

    public String getLineaBaja() {
        return lineaBaja;
    }

    public String getCelular() {
        return celular;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public void setLineaBaja(String lineaBaja) {
        this.lineaBaja = lineaBaja;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public boolean insert() {
        if (-1 != id) {
            System.out.println("Aparentemente este objeto pertenece a un registro en la base de datos, por lo que"
                    + "no puede ser insertado");
            return false;
        }

        String sql = "INSERT INTO Funcionario (nombres, apellidos, ci, lineaBaja, celular) VALUES (?, ?, ?, ?, ?);";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //stm.setInt(1, this.id);
            stm.setString(1, this.nombres);
            stm.setString(2, this.apellidos);
            stm.setString(3, this.ci);
            stm.setString(4, this.lineaBaja);
            stm.setString(5, this.celular);
            stm.executeUpdate();

            ResultSet clave = stm.getGeneratedKeys();
            clave.next();
            id = clave.getInt(1);
            System.out.println("The worker has been added with id: " + id);
        } catch (SQLException ex) {
            Logger.getLogger(DbFuncionario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    public static ArrayList readAll() {
        ArrayList<DbFuncionario> funcionarios = new ArrayList();
        String sql = "SELECT * FROM Funcionario;";

        try (Connection con = ConnectionPool.getConnection(); Statement stm = con.createStatement();) {

            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                int ID = resultado.getInt("id");
                String NOMBRES = resultado.getString("nombres");
                String APELLIDOS = resultado.getString("apellidos");
                String CI = resultado.getString("ci");
                String LINEABAJA = resultado.getString("lineaBaja");
                String CELULAR = resultado.getString("celular");
                funcionarios.add(new DbFuncionario(ID, NOMBRES, APELLIDOS, CI, LINEABAJA, CELULAR));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbFuncionario.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Collections.sort(funcionarios, new SortbyRoll());
        return funcionarios;

    }

    public boolean read(int id) {
        if (-1 != this.id) {
            System.out.println("Al parecer este objeto esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser leido");
            return false;
        }

        String sql = "SELECT * FROM Funcionario WHERE id=" + id;
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet resultado = stm.executeQuery();
            if (resultado.next()) {
                this.id = id;
                nombres = resultado.getString("nombres");
                apellidos = resultado.getString("apellidos");
                ci = resultado.getString("ci");
                lineaBaja = resultado.getString("lineaBaja");
                celular = resultado.getString("celular");
            } else {
                System.out.println("Aparentemente no existe un funcionario con ese id = " + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbFuncionario.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public boolean update() {
        if (-1 == id) {
            System.out.println("Al parecer este objeto no esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser actualizado");
            return false;
        }

        String sql = "UPDATE Funcionario SET nombres = ?, apellidos = ?, ci = ?, lineaBaja = ?, celular = ? where id = ?";
        try (Connection con = ConnectionPool.getConnection()) {
            PreparedStatement stm = con.prepareStatement(sql);
            //stm.setInt(1, this.id);
            stm.setString(1, this.nombres);
            stm.setString(2, this.apellidos);
            stm.setString(3, this.ci);
            stm.setString(4, this.lineaBaja);
            stm.setString(5, this.celular);
            stm.setInt(6, this.id);
            stm.executeUpdate();

            System.out.println("EL trabajador con id = " + id + " ahora tiene los datos (" + nombres + ", " + apellidos + ", " + ci + ", " + lineaBaja + ", " + ", " + celular + ")");
        } catch (SQLException ex) {
            Logger.getLogger(DbFuncionario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

    @Override
    public boolean delete() {
        if (-1 == id) {
            System.out.println("Al parecer este objeto no esta sincronizado con una fila de la "
                    + "base de datos, por lo que no puede ser borrado");
            return false;
        }

        String sql = "DELETE FROM Funcionario WHERE id=" + id;
        try (Connection con = ConnectionPool.getConnection()) {
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            System.out.println("El funcionario con id = " + id + " ha sido eliminado con exito");
            //id = -1;
        } catch (SQLException ex) {
            Logger.getLogger(DbFuncionario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

    // Para reasignar valores por defecto al objeto    
    public void resetValues() {
        id = -1;
        nombres = apellidos = ci = lineaBaja = celular = "";
    }

    @Override
    public ArrayList read() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
