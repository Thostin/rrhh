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
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/horariosrrhh");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(10); // Max 10 active connections
        dataSource = new HikariDataSource(config);
    }
    
    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
}
