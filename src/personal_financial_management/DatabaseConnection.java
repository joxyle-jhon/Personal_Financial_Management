/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package personal_financial_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author k
 */
public class DatabaseConnection {
    private static final String DB_NAME ="personal_financial_management"; //mao ni akong geh pangalan sa database sa PC
    private static final String JDBC_URL ="jdbc:mysql://localhost:3306/"+DB_NAME;
    private static final String USER ="root";
    private static final String PASSWORD =""; //admin ang password sa database ambot lang sa inyo alsida lang
    
    //create a function to get the connection
    public static Connection getConnection(){
            Connection connection = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
                System.out.println("Connected to the database");
            } catch (ClassNotFoundException | SQLException ex) {
                System.out.println("Connection - ClassNotFoundException: " + ex.getMessage());
            }
            return connection;
    }
    
}
