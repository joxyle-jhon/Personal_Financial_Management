/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package personal_financial_management;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author k
 */
public class TransactionDAO {
    
    //Method to retrieve all transactions from the database
    public static List<Transaction>getAllTransaction(){
        List<Transaction>transactions = new ArrayList<>();
        
        Connection connection = DatabaseConnection.getConnection();
        
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = connection.prepareStatement("SELECT * FROM `transaction_table`");
            rs = ps.executeQuery();
        //Iterate through the result set obtained from the SQL query
        while (rs.next()){
            int id = rs.getInt("id");
            String type = rs.getString("transaction_type");
            String description = rs.getString("description");
            double amount = rs.getDouble("amount");
            
            Transaction transaction = new Transaction(id, type, description, amount);
            transactions.add(transaction);
        }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return transactions;
    }
}