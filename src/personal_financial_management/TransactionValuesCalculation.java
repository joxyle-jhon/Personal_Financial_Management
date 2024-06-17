/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package personal_financial_management;

import java.util.List;
/**
 *
 * @author k
 */
public class TransactionValuesCalculation {
    
    // Method to calculate the total incomes from a list of transaction
    public static Double getTotalIncomes(List<Transaction> transactions){
        //Initialize the total income variable
        double totalIncome = 0.0;
        //Loop through each transaction in the List
        for (Transaction transaction : transactions){
            //Check if the transaction type is "Income"
            if("Income".equals(transaction.getType())){
                totalIncome += transaction.getAmount();
            }
        }
        return totalIncome;
    }
    
    // Method to calculate the total expense from a list of transaction
    public static Double getTotalExpenses(List<Transaction> transactions){
        //Initialize the total expense variable
        double totalExpenses = 0.0;
        //Loop through each transaction in the List
        for (Transaction transaction : transactions){
            //Check if the transaction type is "Expense"
            if("Expense".equals(transaction.getType())){
                totalExpenses += transaction.getAmount();
            }
        }
        return totalExpenses;
    }
    
    // Method to calculate the total value (income - expense) from a list of transactions
    public static Double getTotalValue(List<Transaction>transactions){
        // Calculate the total income using the getTotalIncomes method
        Double totalIncome = getTotalIncomes(transactions);
        // Calculate the total expenses using the getTotalIncomes method
        Double totalExpense = getTotalExpenses(transactions);
        // Return the calculated total value
        return totalIncome - totalExpense;
    }
    
    
}
