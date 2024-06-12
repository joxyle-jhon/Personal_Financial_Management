/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package personal_financial_management;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;





/**
 *
 * @author k
 */
public class ExpenseIncomeTrackerApp {
    
    //Varriable for main frameUI
    private JFrame frame;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JLabel closeLabel;
    private JLabel minimizeLabel;
    private JPanel dashboardPanel;
    private JPanel buttonsPanel;
    private JButton addTransactionButton;
    private JButton removeTransactionButton;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    //Variable to store the total number
    private double totalAmmount = 0.0;
    
    //ArrayList to store data panel values
    private ArrayList<String> dataPanelValues = new ArrayList<>();
    
    //variable for variable dragging
    private boolean isDragging = false;
    private Point mouseOffset;
    
    // Constructor
    public ExpenseIncomeTrackerApp() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,700);
        frame.setLocationRelativeTo(null);
        
        // Remove border of the frame
        frame.setUndecorated(true);
        
        // Set Custom border to the  frame
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(35, 35, 35)));
        
        // Create and set up the title bar
        titleBar = new JPanel();
        titleBar.setLayout(null);
        titleBar.setBackground(new Color(148, 226, 124));
        titleBar.setPreferredSize(new Dimension(frame.getWidth(), 30));
        frame.add(titleBar, BorderLayout.NORTH);
        
        // Create and set up the title label
        titleLabel = new JLabel("Personal Finace Management");
        titleLabel.setForeground(Color.BLACK);
        titleLabel .setFont(new Font("Arial", Font.BOLD, 17));
        titleLabel.setBounds(10, 0, 300, 30);  // Set bounds for the title label
        titleBar.add(titleLabel);
        
        
        // Create and Setup close label
        closeLabel = new JLabel("x");
        closeLabel.setForeground(Color.BLACK);
        closeLabel.setFont(new Font("Arial", Font.BOLD, 17));
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        closeLabel.setBounds(frame.getWidth() - 40, 0, 30, 30);  // Set bounds for the close label
        titleBar.add(closeLabel);
        
        
        // Set frame to visible
        frame.setVisible(true);

        
    }

    public static void main(String[] args) {
        new ExpenseIncomeTrackerApp();
    }
    
    
    
}
