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

public class ExpenseIncomeTrackerApp {

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

        removeTransactionButton = new JButton("Remove Transaction");
        removeTransactionButton.setBackground(new Color(76, 154, 42));
        removeTransactionButton.setForeground(Color.WHITE);
        removeTransactionButton.setFocusPainted(false);
        removeTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout(10, 5));
        buttonsPanel.add(addTransactionButton, BorderLayout.NORTH);
        buttonsPanel.add(removeTransactionButton, BorderLayout.SOUTH);
        dashboardPanel.add(buttonsPanel);

        String[] columnNames = {"ID", "Type", "Description", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0); // Initialize with 0 rows
        transactionTable = new JTable(tableModel);
        configureTransactionTable();
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        configureScrollPane(scrollPane);
        dashboardPanel.add(scrollPane);

        frame.setVisible(true);
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
        
        // Add components to dialog panel
        dialogPanel.add(typeLabel);
        dialogPanel.add(typeCombobox);
        dialogPanel.add(descriptionLabel);
        dialogPanel.add(descriptionField);
        dialogPanel.add(amountLabel);
        dialogPanel.add(amountField);
        dialogPanel.add(new JLabel());        
        dialogPanel.add(addButton);
        
        
                
        
        dialog.add(dialogPanel);
        dialog.setVisible(true);
    }
    

    private void configureTransactionTable() {
        transactionTable.setBackground(new Color(236, 240, 241));
        transactionTable.setRowHeight(30);
        transactionTable.setShowGrid(false);
        transactionTable.setBorder(null);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
    }
    
    
    
    // Add a data panel to the dashboard panel
    private void addDataPanel(String title, int index) {
        JPanel dataPanel = new JPanel() {
            
            // Override the paintComponent method to customize the appearance
            @Override
            protected void paintComponent(Graphics g) {
                // Call the paintComponent method of the superclass
                super.paintComponent(g);
                // Check if the title is "Total" to determine the content to display
                if (title.equals("Total")) {
                    drawDataPanel(g, title, String.format("₱%,.2f", totalAmount), getWidth(), getHeight());
                } else {
                    drawDataPanel(g, title, dataPanelValues.size() > index ? dataPanelValues.get(index) : "00", getWidth(), getHeight());
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

class CustomScrollBarUI extends BasicScrollBarUI {
    private Color thumbColor = new Color(189, 195, 199);
    private Color trackColor = new Color(236, 240, 241);
    
    @Override
    protected void configureScrollBarColors(){
        super.configureScrollBarColors();
        
    }
    
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createEmptyButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createEmptyButton();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(thumbColor);
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(trackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
    
    private JButton createEmptyButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        return button;
    }
}

