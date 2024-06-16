/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package personal_financial_management;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author k
 */
public class DatabaseConnection {
    private static final String DB_NAME ="";
    private static final String JDBC_URL ="jdbc:mysql://locahost:3306/"+DB_NAME;
    private static final String USER ="root";
    private static final String PASSWORD ="";
    
    //create a function to get the connection
    public static Connection getConnection(){
            Connection connection = null;
    }
    
}
