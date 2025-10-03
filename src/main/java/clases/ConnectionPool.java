/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * @author thotstin
 */
public class ConnectionPool {
    private static HikariDataSource dataSource;
    
    public static void init(String username, String password){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://186.17.106.181:3100/horariosrrhh");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10); // Max 10 active connections
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
}
