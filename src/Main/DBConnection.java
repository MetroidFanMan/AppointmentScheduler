/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.sql.*;

/**
 *
 * @author Ethan
 */
public class DBConnection {
    
    private static final String DB_NAME = "U04fin";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/" + DB_NAME;
    private static final String USERNAME = "U04fin";
    private static final String PASSWORD = "53688227557";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public static Connection conn;
    
    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception {
        
        Class.forName(DRIVER);
        
        conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        System.out.println("Connection successful");
        
    }
    
    public static void closeConnection() throws ClassNotFoundException, SQLException, Exception {
        
        conn.close();
        System.out.println("Connection closed");
        
    }
    
}
