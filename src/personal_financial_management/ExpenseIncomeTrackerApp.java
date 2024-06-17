package personal_financial_management;

import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;


public class ExpenseIncomeTrackerApp {

    private JButton exportButton;
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

    private double totalAmount = 0.0;

    private ArrayList<String> dataPanelValues = new ArrayList<>();

    private boolean isDragging = false;
    private Point mouseOffset;

    public ExpenseIncomeTrackerApp() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(35, 35, 35)));

        titleBar = new JPanel();
        titleBar.setLayout(null);
        titleBar.setBackground(new Color(148, 226, 124));
        titleBar.setPreferredSize(new Dimension(frame.getWidth(), 30));
        frame.add(titleBar, BorderLayout.NORTH);

        titleLabel = new JLabel("Personal Finance Management");
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 17));
        titleLabel.setBounds(10, 0, 300, 30);
        titleBar.add(titleLabel);

        closeLabel = new JLabel("x");
        closeLabel.setForeground(Color.BLACK);
        closeLabel.setFont(new Font("Arial", Font.BOLD, 17));
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        closeLabel.setBounds(frame.getWidth() - 40, 0, 30, 30);
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleBar.add(closeLabel);

        minimizeLabel = new JLabel("-");
        minimizeLabel.setForeground(Color.BLACK);
        minimizeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        minimizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        minimizeLabel.setBounds(frame.getWidth() - 70, 0, 30, 30);
        minimizeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleBar.add(minimizeLabel);

        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(Color.BLACK);
            }
        });

        minimizeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setState(JFrame.ICONIFIED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                minimizeLabel.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                minimizeLabel.setForeground(Color.BLACK);
            }
        });

        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                isDragging = true;
                mouseOffset = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });

        titleBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    Point newLocation = e.getLocationOnScreen();
                    newLocation.translate(-mouseOffset.x, -mouseOffset.y);
                    frame.setLocation(newLocation);
                }
            }
        });

        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout());      
        dashboardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        dashboardPanel.setBackground(new Color(226, 240, 241));
        frame.add(dashboardPanel, BorderLayout.CENTER);
        
        // Calculate the total amount and populate data panel values
        totalAmount = TransactionValuesCalculation.getTotalValue(TransactionDAO.getAllTransaction());
        dataPanelValues.add(String.format("₱%,.2f", TransactionValuesCalculation.getTotalExpenses(TransactionDAO.getAllTransaction())));
        dataPanelValues.add(String.format("₱%,.2f", TransactionValuesCalculation.getTotalIncomes(TransactionDAO.getAllTransaction())));
        dataPanelValues.add("₱"+totalAmount);
        
        
        
        
        
        // Add data panels for Expense, Income, and Total
        addDataPanel("Expense", 0);
        addDataPanel("Income", 1);
        addDataPanel("Total", 2);

        addTransactionButton = new JButton("Add Transaction");
        addTransactionButton.setBackground(new Color(30, 86, 49));
        addTransactionButton.setForeground(Color.WHITE);
        addTransactionButton.setFocusPainted(false);
        addTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
        addTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addTransactionButton.addActionListener((e) -> { showAddTransactionDialog(); });
        // Inside the ExpenseIncomeTrackerApp class constructor
        addTransactionButton.addActionListener((e) -> { 
            // Add action listener to the "Add Transaction" button
            System.out.println("Add Transaction button clicked"); // Print a message indicating that the button was clicked
            showAddTransactionDialog(); // Call the method to display the add transaction dialog
        });
        
        

        removeTransactionButton = new JButton("Remove Transaction");
        removeTransactionButton.setBackground(new Color(30, 86, 49));
        removeTransactionButton.setForeground(Color.WHITE);
        removeTransactionButton.setFocusPainted(false);
        removeTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeTransactionButton.addActionListener((e) -> {
            removeSelectedTransaction();
        });
        // Inside the ExpenseIncomeTrackerApp class constructor
        removeTransactionButton.addActionListener((e) -> {
            // Add action listener to the "Remove Transaction" button
            System.out.println("Remove Transaction button clicked"); // Print a message indicating that the button was clicked
            removeSelectedTransaction(); // Call the method to remove the selected transaction
        });

        
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout(10, 5));
        buttonsPanel.add(addTransactionButton, BorderLayout.NORTH);
        buttonsPanel.add(removeTransactionButton, BorderLayout.SOUTH);
        dashboardPanel.add(buttonsPanel);
        
        exportButton = new JButton("Export");
        exportButton.setBackground(new Color(10, 173, 255));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            System.out.println("Export Successfully!");
            }
            
        });
               
        
        buttonsPanel.add(exportButton);        

        String[] columnNames = {"ID", "Type", "Description", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0); // Initialize with 0 rows
        transactionTable = new JTable(tableModel);
        configureTransactionTable();
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        configureScrollPane(scrollPane);
        dashboardPanel.add(scrollPane);
        
       
        

        frame.setVisible(true);
    }
    
    
    
    
    
    
    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {
    try {
        exportToCSV();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    // Exporting Files
    private void exportToCSV() throws IOException {
        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/Downloads/Personal-Finacial-Record.csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

        // Write header
        for (int i = 0; i < transactionTable.getColumnCount(); i++) {
            bw.write(transactionTable.getColumnName(i) + ",");
        }
        bw.newLine();

        // Write data
        for (int i = 0; i < transactionTable.getRowCount(); i++) {
            for (int j = 0; j < transactionTable.getColumnCount(); j++) {
                bw.write(transactionTable.getValueAt(i, j).toString() + ",");
            }
            bw.newLine();
        }

        bw.flush();
        bw.close();
        JOptionPane.showMessageDialog(null, "Data exported successfully to " + filePath);
    }



    
    private void printTransactions() {
    // Get all transactions from the database
    List<Transaction> transactions = TransactionDAO.getAllTransaction();

    // Print each transaction
    for (Transaction transaction : transactions) {
        System.out.println("Transaction ID: " + transaction.getId());
        System.out.println("Type: " + transaction.getType());
        System.out.println("Description: " + transaction.getDescription());
        System.out.println("Amount: " + transaction.getAmount());
        System.out.println();
    }
}

    
    
    // fix the negative value
    private String fixNegativeValueDisplay(double value) {
        String newVal = String.format("₱%,. 2f", value);
        if(newVal.startsWith("₱-")){
            String numericPart = newVal.substring(2);
            newVal = "-₱"+numericPart;
        }
        return newVal;
    }
          
    
    
    
    
    // Remove Selected transaction from the table and database
    private void removeSelectedTransaction(){
        int selectedRow = transactionTable.getSelectedRow();
        
        if (selectedRow != -1){
            int transactionId = (int) transactionTable.getValueAt(selectedRow, 0);
            String type = transactionTable.getValueAt(selectedRow, 1).toString();
            String amountStr = transactionTable.getValueAt(selectedRow, 3).toString();
            double amount = Double.parseDouble(amountStr.replace("₱", "").replace(" ", "").replace(",", "").replace("--", "-"));
            
            // Update totalAmount based on the type of transaction
            if(type.equals("Income")) { totalAmount -= amount; }
            else { totalAmount -= amount; }
            
            JPanel totalPanel = (JPanel) dashboardPanel.getComponent(2);
            totalPanel.repaint();
            
            int indexToUpdate = type.equals("Income") ? 1 : 0;
            
            
            
            String currentValue = dataPanelValues.get(indexToUpdate);
            double currentAmount = Double.parseDouble(currentValue.replace("₱", "").replace(" ", "").replace(",", ""));
            double updatedAmount = currentAmount + (type.equals("Income") ? -amount : amount);
            dataPanelValues.set(indexToUpdate, String.format("₱%,.2f", updatedAmount));
            
            JPanel dataPanel = (JPanel) dashboardPanel.getComponent(indexToUpdate);
            dataPanel.repaint();
            
            //Remove Selected row from table model
            tableModel.removeRow(selectedRow);
            removeTransactionFromDatabase(transactionId);
            
 
            
        }
    }
    
    private void exportData() {
        // Get all transactions from the database
        List<Transaction> transactions = TransactionDAO.getAllTransaction();

        // Define the file name and path
        String fileName = "transactions.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write column headers
            writer.write("ID,Type,Description,Amount");
            writer.newLine();

            // Write each transaction
            for (Transaction transaction : transactions) {
                writer.write(transaction.getId() + "," +
                             transaction.getType() + "," +
                             transaction.getDescription() + "," +
                             transaction.getAmount());
                writer.newLine();
            }

            // Show success message
            JOptionPane.showMessageDialog(frame, "Data exported successfully to " + fileName, "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            // Show error message if export fails
            JOptionPane.showMessageDialog(frame, "Error exporting data: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    // Remove transaction
    private void removeTransactionFromDatabase(int transactionId){
        
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `transaction_table` WHERE `id` = ?");
            
            ps.setInt(1, transactionId);
            ps.executeLargeUpdate();
            System.out.println("Transaction Removed!");
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ExpenseIncomeTrackerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Display the dialog for Adding a new transaction
    private void showAddTransactionDialog(){
        
        //Create a new JDialog for adding transaction
        JDialog dialog = new JDialog(frame, "Add Transaction", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(frame);
        
        //Create a panel components to hold the components in a grid layout
        JPanel dialogPanel = new JPanel(new GridLayout(4, 0, 10, 10));
        
        // Set an empty border with padding for the dialog panel
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        dialogPanel.setBackground(new Color(172,223,135,255));
        
        // Create and configure components for transaction input
        JLabel typeLabel = new JLabel("Type");
        JComboBox<String> typeCombobox = new JComboBox<>(new String[]{"Expense", "Income"});
        typeCombobox.setBackground(Color.WHITE);
//        typeCombobox.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        
        JLabel descriptionLabel = new JLabel("Description");
        JTextField descriptionField = new JTextField();
        
        JLabel amountLabel = new JLabel("Amount");
        JTextField amountField = new JTextField();
        
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(30,86,49,255));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        


        // Add the export button below the table panel
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exportPanel.setBackground(new Color(226, 240, 241));
        exportPanel.add(exportButton);

        dashboardPanel.add(exportPanel);


        
        // Inside the ExpenseIncomeTrackerApp constructor after adding other buttons
        JButton printButton = new JButton("Print Transactions");
        printButton.setBackground(new Color(59, 89, 182));
        printButton.setForeground(Color.WHITE);
        printButton.setFocusPainted(false);
        printButton.setFont(new Font("Arial", Font.BOLD, 14));
        printButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        printButton.addActionListener((e) -> {
            printTransactions();
        });

buttonsPanel.add(printButton, BorderLayout.CENTER);
        
        
        // Insert Button
        addButton.addActionListener((e) -> {
            addTransaction(typeCombobox, descriptionField, amountField);
        });
        
        // Add components to dialog panel
        dialogPanel.add(typeLabel);
        dialogPanel.add(typeCombobox);
        dialogPanel.add(descriptionLabel);
        dialogPanel.add(descriptionField);
        dialogPanel.add(amountLabel);
        dialogPanel.add(amountField);
        dialogPanel.add(new JLabel());        
        dialogPanel.add(addButton);
        
        //Database Connection
        DatabaseConnection.getConnection();
                
        dialog.add(dialogPanel);
        dialog.setVisible(true);
    }
    
    // Add new transaction to database
private void addTransaction(JComboBox<String> typeCombobox, JTextField descriptionField, JTextField amountField) {

    // Retrieve transaction details from the input fields
    String type = (String) typeCombobox.getSelectedItem();
    String description = descriptionField.getText();
    String amount = amountField.getText();

    // Parse the amount = string to a double value
    double newAmount = Double.parseDouble(amount.replace("₱", "").replace(" ", "").replace(",", ""));

    // Update the total amount based on the transaction type (Income or Expenses)
    if (type.equals("Income")) {
        totalAmount += newAmount;
    } else { // Expense
        totalAmount -= newAmount;
    }

    // Update the displayed total amount on the dashboard panel
    JPanel totalPanel = (JPanel) dashboardPanel.getComponent(2);
    totalPanel.repaint();

    // Determine the index of the data panel to update based on the transaction type
    int indexToUpdate = type.equals("Income") ? 1 : 0;

    // Retrieve the current value of the data panel
    String currentValue = dataPanelValues.get(indexToUpdate);

    // Parse the current amount string to a double value
    double currentAmount = Double.parseDouble(currentValue.replace("₱", "").replace(" ", "").replace(",", ""));

    // Calculate the updated amount based on the transaction type
    double updatedAmount = currentAmount + newAmount;

    // Update the data panel with the new amount
    dataPanelValues.set(indexToUpdate, String.format("₱%,.2f", updatedAmount));

    // Update the displayed data panel on the dashboard panel
    JPanel dataPanel = (JPanel) dashboardPanel.getComponent(indexToUpdate);
    dataPanel.repaint();

    try {
        Connection connection = DatabaseConnection.getConnection();
        String insertQuery = "INSERT INTO `transaction_table`(`transaction_type`, `description`, `amount`) VALUES (?,?,?)";
        PreparedStatement ps = connection.prepareStatement(insertQuery);

        ps.setString(1, type);
        ps.setString(2, description);
        ps.setDouble(3, Double.parseDouble(amount));
        ps.executeUpdate();
        System.out.println("Data inserted successfully!");
        
        tableModel.setRowCount(0);
        populateTableTransactions();

    } catch (SQLException ex) {
        System.out.println("Error: Data not inserted!");
    }
}

// Populate Table Transactions
private void populateTableTransactions() {
    // Clear existing data from the table model
    tableModel.setRowCount(0);

    // Get all transactions from the database
    List<Transaction> transactions = TransactionDAO.getAllTransaction();

    // Iterate through each transaction and add it to the table model
    for (Transaction transaction : transactions) {
        Object[] rowData = {transaction.getId(), transaction.getType(),
                transaction.getDescription(), transaction.getAmount()
        };
        tableModel.addRow(rowData);
    }
}



    private void configureTransactionTable() {
        transactionTable.setBackground(new Color(236, 240, 241));
        transactionTable.setRowHeight(30);
        transactionTable.setShowGrid(false);
        transactionTable.setBorder(null);
        transactionTable.setDefaultRenderer(Object.class, new TransactionTableCellRenderer());
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        populateTableTransactions(); //---------------------------------------------------------------------
       
        

        JTableHeader tableHeader = transactionTable.getTableHeader();
        
        tableHeader.setBackground(new Color(30, 86, 49));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 18));
        tableHeader.setDefaultRenderer(new GradientHeaderRenderer());
    }
    
    private void configureScrollPane(JScrollPane scrollPane) {
        scrollPane.setPreferredSize(new Dimension(850, 300)); // Set the size of the Panel
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI()); 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
    }
    
    
    
    // Add a data panel to the dashboard panel
    private void addDataPanel(String title, int index) {
        JPanel dataPanel = new JPanel() {
            
            // Override the paintComponent method to customize the appearance
            @Override
            protected void paintComponent(Graphics g) {
                // Call the paintComponent method of the superclass
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Make the drawing smooth
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Check if the title is "Total" to determine the content to display
                if (title.equals("Total")) {
                    drawDataPanel(g2d, title, String.format("₱%,.2f", totalAmount), getWidth(), getHeight());
                } else {
                    drawDataPanel(g2d, title, dataPanelValues.get(index), getWidth(), getHeight());
                }
            }
        };

        // Set layout, size, background color, and border for the data panel
        dataPanel.setLayout(new GridLayout(2, 1));
        dataPanel.setPreferredSize(new Dimension(170, 100));
        dataPanel.setBackground(new Color(225, 225, 255));
        dataPanel.setBorder(new LineBorder(new Color(149, 165, 166), 2));

        dashboardPanel.add(dataPanel);
        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }

    // Method to draw custom data panel content
    private void drawDataPanel(Graphics g, String title, String value, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw panel background
        g2d.setColor(new Color(104, 187, 89));
        g2d.fillRoundRect(0, 0, width, height, 20, 20);

        // Draw header background
        g2d.setColor(new Color(172, 223, 135));
        g2d.fillRect(0, 0, width, 40);

        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(title, 20, 30);

        // Draw value
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.drawString(value, 20, 75);
    }

    // Main method
    public static void main(String[] args) {
        new ExpenseIncomeTrackerApp();
        
    }

    
    
    
    // Custom table header renderer with gradient background
    class GradientHeaderRenderer extends JLabel implements TableCellRenderer {

        private Color startColor = new Color(104, 187, 89, 255);
        private Color endColor = new Color(104,187,89,255);

        
        public GradientHeaderRenderer() {
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Arial", Font.BOLD, 22));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GREEN),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
            ));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            int width = getWidth();
            int height = getHeight();

            GradientPaint gradientPaint = new GradientPaint(
                    0, 0, startColor, width, 0, endColor
            );

            g2d.setPaint(gradientPaint);
            g2d.fillRect(0, 0, width, height);

            super.paintComponent(g);
        }
   }
    
    
}

class CustomScrollBarUI extends BasicScrollBarUI{
        // Colors for the thumb and track of the scroll bar
        private Color thumbColor = Color.gray;
        private Color trackColor = new Color(254,254,254);
   
        // Override method to configure the scroll bar colors
        @Override
        protected void configureScrollBarColors(){
            // Call the superclass method to ensure default configuration
            super.configureScrollBarColors();
            
        }
        
        // Override method to create the decrease button of the scroll bar
        @Override
        protected JButton createDecreaseButton(int orientation){
            // Create an empty button for the decrease button
            return createEmptyButton();
        }
        
        // Override method to create the increase button of the scroll bar
        @Override
        protected JButton createIncreaseButton(int orientation){
            // Create an empty button for the increase button
            return createEmptyButton();
        }
        
        // Override method to paint the thumb of the scroll bar
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds){
            // Set the color and fill the thumb area with the specified color
            g.setColor(thumbColor);
            g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        }
        
        // Override method to paint the track of the scroll bar
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds){
            // Set the color and fill the track area with the specified color
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
        
        // Private method to create an empty button with zero dimensions
        private JButton createEmptyButton(){
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            return button;
        }
        
}

// Custom cell renderer for the transaction table
class TransactionTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String type = (String) table.getValueAt(row, 1);

        if (isSelected) {
            c.setBackground(new Color(255, 255, 255)); // Set the background color for selected rows
            c.setForeground(new Color(1, 1, 1));
        } else {
            if ("Income".equals(type)) {
                c.setBackground(new Color(252, 240, 3)); // Set the background color for income rows
                c.setForeground(Color.BLACK); // Set the foreground color for income rows
            } else {
                c.setBackground(new Color(252, 198, 3)); // Set the background color for expense rows
                c.setForeground(Color.BLACK); // Set the foreground color for expense rows
            }
        }
        return c;
    }
}


