package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.sql.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffDashboardScreen extends JFrame {

    private DefaultTableModel tableModel;

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public StaffDashboardScreen() {
        setTitle("Staff Dashboard - Trains of Sheffield");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        initializeUI();
    }


   
    private void initializeUI() {
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Create different panels here
        mainPanel.add(createOrdersPanel(), "Orders");
        mainPanel.add(createInventoryPanel(), "Inventory");

        // Set layout for the entire frame
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(createSidebar(), BorderLayout.WEST);
    }



    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(e -> {
            dispose(); // Close current window
            new Login().setVisible(true);
        });

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            dispose(); // Close current window
            new Home().setVisible(true);
        });

        topPanel.add(signOutButton, BorderLayout.WEST);
        topPanel.add(homeButton, BorderLayout.EAST);
        return topPanel;
    }

    private JPanel createOrdersPanel() {
        JPanel ordersPanel = new JPanel(new BorderLayout());

        // Fetch data from the database
        DefaultTableModel ordersModel = fetchDataFromDatabaseForOrders();
    
        // Create the table using the retrieved data
        JTable ordersTable = new JTable(ordersModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Buttons for order management
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton fulfillButton = new JButton("Fulfill Order");
        JButton deleteOrderButton = new JButton("Delete Order");
        buttonPanel.add(fulfillButton);
        buttonPanel.add(deleteOrderButton);
    
        fulfillButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                Object orderId = ordersTable.getValueAt(selectedRow, 0);
                //for saif to add the functionality of the buttons
            } 
            else {
                JOptionPane.showMessageDialog(null, "No order selected for to be fulfilled.");
            }
        });
    
        deleteOrderButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                Object orderId = ordersTable.getValueAt(selectedRow, 0);
                //for saif to add the functionality of the buttons
            } 
            else {
                JOptionPane.showMessageDialog(null, "No order selected for deletion.");

            }
        });
    
        // Add button panel to the ordersPanel
        ordersPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        return ordersPanel;
    }

    private DefaultTableModel fetchDataFromDatabaseForOrders() {
        DefaultTableModel ordersModel = new DefaultTableModel();
    
        try {
            // Establish database connection
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement stmt = connection.createStatement();
    
            // Modify the query to select specific columns
            ResultSet rs = stmt.executeQuery("SELECT OrderLineID, Quantity, LineCost, ProductCode, Status FROM OrderLine");
    
            // Add columns to the table model
            ordersModel.addColumn("Order ID");
            ordersModel.addColumn("Quantity");
            ordersModel.addColumn("Line Cost");
            ordersModel.addColumn("Product Code");
            ordersModel.addColumn("Status");

    
            // Populate table with data
            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("OrderLineID"),
                    rs.getInt("Quantity"),
                    rs.getDouble("Quantity"),
                    rs.getString("ProductCode"),
                    rs.getString("Status")
                };
                ordersModel.addRow(rowData);
            }
    
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return ordersModel;
    }




    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());

        // Fetch data from the database
        DefaultTableModel inventoryModel = fetchDataFromDatabaseForInventory();
    
        // Create the table using the retrieved data
        JTable inventoryTable = new JTable(inventoryModel);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add New Item");
        JButton editButton = new JButton("Edit Selected Item");
        JButton deleteButton = new JButton("Delete Selected Item");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(e -> {

        });
    
        editButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                Object itemId = inventoryTable.getValueAt(selectedRow, 0);

            } 
            else {
                JOptionPane.showMessageDialog(null, "No order selected for deletion.");
            }
        });
    
        deleteButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                Object itemId = inventoryTable.getValueAt(selectedRow, 0);

            } 
            else {
                JOptionPane.showMessageDialog(null, "No order selected for deletion.");
            }
        });
    
        // Add button panel to the inventoryPanel
        inventoryPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        return inventoryPanel;
    }

    private DefaultTableModel fetchDataFromDatabaseForInventory() {
        DefaultTableModel inventoryModel = new DefaultTableModel();
    

        try {
            // Establish database connection
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement stmt = connection.createStatement();
    
            // Modify the query to select specific columns
            ResultSet rs = stmt.executeQuery("SELECT ProductCode, BrandName, ProductName, FeatureCode, Stock FROM Product");
    
            // Add columns to the table model
            inventoryModel.addColumn("Product Code");
            inventoryModel.addColumn("Brand Name");
            inventoryModel.addColumn("Product Name");
            inventoryModel.addColumn("Feature Code");
            inventoryModel.addColumn("Stock");
    
            // Populate table with data
            while (rs.next()) {
                Object[] rowData = {
                    rs.getString("ProductCode"),
                    rs.getString("BrandName"),
                    rs.getString("FeatureCode"),
                    rs.getString("ProductName"),
                    rs.getInt("Stock")
                };
                inventoryModel.addRow(rowData);
            }
    
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return inventoryModel;
    }


    private JPanel createSidebar() {
        // Create a panel for the sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Create buttons for the sidebar
        JButton ordersButton = new JButton("Orders");
        JButton inventoryButton = new JButton("Inventory");

        // Add action listeners to buttons
        ordersButton.addActionListener(e -> cardLayout.show(mainPanel, "Orders"));
        inventoryButton.addActionListener(e -> cardLayout.show(mainPanel, "Inventory"));

        // Add buttons to the sidebar
        sidebar.add(ordersButton);
        sidebar.add(inventoryButton);

        return sidebar;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StaffDashboardScreen().setVisible(true);
        });
    }
    
}
