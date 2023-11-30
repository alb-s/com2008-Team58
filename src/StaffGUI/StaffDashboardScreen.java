package StaffGUI;

import CustomerGUI.HomeScreen;
import CustomerGUI.LoginScreen;
import CustomerGUI.Session;
import ManagerGUI.HomeManager;

import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.*;

public class StaffDashboardScreen extends JFrame {
    private JTable ordersTable; // Class level variable for orders table
    private JTable inventoryTable; // Class level variable for inventory table
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public StaffDashboardScreen() {
        setTitle("Staff Dashboard - Trains of Sheffield");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUI();
    }
    private void initializeUI() {
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.add(createInventoryPanel(), "Inventory");
        mainPanel.add(createOrdersPanel(), "Orders");
        mainPanel.add(createArchivePanel(), "Archive");


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
            new LoginScreen().setVisible(true);
        });
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
           dohome();
        });
        topPanel.add(signOutButton, BorderLayout.EAST);
        topPanel.add(homeButton, BorderLayout.WEST);
        return topPanel;
    }
    private void dohome(){
        String Role = Session.getInstance().getUserRole();
        if (Role.equals("Manager")) {
            dispose();
            HomeManager HomeManager = new HomeManager();
            HomeManager.setVisible(true);
        } else if (Role.equals("Staff")) {
            dispose();
            staffView staffView = new staffView();
            staffView.setVisible(true);
        }
        else{
            dispose();
            HomeScreen HomeScreen = new HomeScreen();
            HomeScreen.setVisible(true);;
        }

    }
    private JPanel createArchivePanel () {
        JPanel archivePanel = new JPanel(new BorderLayout());
        DefaultTableModel archiveModel = fetchDataFromDatabaseForArchive();
        JTable archiveTable = new JTable(archiveModel);
        JScrollPane scrollPane = new JScrollPane(archiveTable);
        archivePanel.add(scrollPane, BorderLayout.CENTER);
        return archivePanel;

    }

    private DefaultTableModel fetchDataFromDatabaseForArchive() {
        DefaultTableModel ordersModel = new DefaultTableModel();

        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement stmt = connection.createStatement();
            String query = "SELECT OrderLineID, Quantity, LineCost, ProductCode, Status, userID, order_date FROM OrderLine WHERE Status = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,"Fulfiled");

            ordersModel.addColumn("Order ID");
            ordersModel.addColumn("Quantity");
            ordersModel.addColumn("Line Cost");
            ordersModel.addColumn("Product Code");
            ordersModel.addColumn("Order Date");
            ordersModel.addColumn("Status");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("OrderLineID"),
                        rs.getInt("Quantity"),
                        rs.getDouble("LineCost"),
                        rs.getString("ProductCode"),
                        rs.getTimestamp("order_date"),
                        rs.getString("Status"),
                        rs.getString("userID")
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

    private JPanel createOrdersPanel() {
        JPanel ordersPanel = new JPanel(new BorderLayout());

        DefaultTableModel ordersModel = fetchDataFromDatabaseForOrders();
        ordersTable = new JTable(ordersModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton deleteOrderButton = new JButton("Delete Order");
        JButton setPendingButton = new JButton("Fulfill Order");
        buttonPanel.add(deleteOrderButton);
        buttonPanel.add(setPendingButton);
        deleteOrderButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this order?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Object orderId = ordersTable.getValueAt(selectedRow, 0);
                    deleteOrder(orderId);
                    refreshOrdersTable();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No order selected.");
            }
        });
        setPendingButton.addActionListener(e -> {
            int selectedRow = ordersTable.getSelectedRow();
            if (selectedRow != -1) {
                Object orderId = ordersTable.getValueAt(selectedRow, 0);
                updateOrderStatusToPending(orderId);
                refreshOrdersTable();

            } else {
                JOptionPane.showMessageDialog(null, "No order selected.");
            }
        });
        ordersPanel.add(buttonPanel, BorderLayout.SOUTH);
        return ordersPanel;
    }



    private DefaultTableModel fetchDataFromDatabaseForOrders() {
        DefaultTableModel ordersModel = new DefaultTableModel();

        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            
            // Use PreparedStatement to prevent SQL injection and specify the status "Confirmed"
            String query = "SELECT OrderLineID, Quantity, LineCost, ProductCode, Status, userID, order_date FROM OrderLine WHERE Status = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "Confirmed");
    
            ResultSet rs = pstmt.executeQuery();
            ordersModel.addColumn("Order ID");
            ordersModel.addColumn("Quantity");
            ordersModel.addColumn("Line Cost");
            ordersModel.addColumn("Product Code");
            ordersModel.addColumn("Order Date");
            ordersModel.addColumn("Status");
    
            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("OrderLineID"),
                    rs.getInt("Quantity"),
                    rs.getDouble("LineCost"),
                    rs.getString("ProductCode"),
                    rs.getTimestamp("order_date"),
                    rs.getString("Status"),
                    rs.getString("userID")
                };
                ordersModel.addRow(rowData);
            }
    
            rs.close();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordersModel;
    }

    private void deleteOrder(Object orderId) {
        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "DELETE FROM OrderLine WHERE OrderLineID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setObject(1, orderId);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateOrderStatusToPending(Object orderId) {
        Date orderDate = new Date();
        Timestamp orderTimestamp = new Timestamp(orderDate.getTime());

        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "UPDATE OrderLine SET Status = 'Fulfiled', order_date = ? WHERE OrderLineID = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setTimestamp(1, orderTimestamp);
            pstmt.setObject(2, orderId);
            pstmt.executeUpdate();
            pstmt.close();

            String orderInfoQuery = "SELECT ProductCode, Quantity FROM OrderLine WHERE OrderLineID = ?";
            PreparedStatement getOrderIQ = connection.prepareStatement(orderInfoQuery);
            getOrderIQ.setObject(1,orderId);
            ResultSet orderResultSet = getOrderIQ.executeQuery();

            if (orderResultSet.next()) {
                String productCode = orderResultSet.getString("ProductCode");
                int orderedQuantity = orderResultSet.getInt("Quantity");
    
                // Update the stock quantity in the Product table
                String updateStockQuery = "UPDATE Product SET Stock = Stock - ? WHERE ProductCode = ?";
                PreparedStatement updateStockStmt = connection.prepareStatement(updateStockQuery);
                updateStockStmt.setInt(1, orderedQuantity);
                updateStockStmt.setString(2, productCode);
                updateStockStmt.executeUpdate();
                updateStockStmt.close();
            }
            orderResultSet.close();
            getOrderIQ.close();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void refreshOrdersTable() {
        DefaultTableModel model = fetchDataFromDatabaseForOrders();
        ordersTable.setModel(model);
    }


    private JPanel createInventoryPanel() {
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        DefaultTableModel inventoryModel = fetchDataFromDatabaseForInventory();

        inventoryTable = new JTable(inventoryModel); // Initialize the inventoryTable
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        inventoryPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton updateStockButton = new JButton("Update Stock");
        buttonPanel.add(updateStockButton);

        JButton productButton = new JButton("Add New Product");
        buttonPanel.add(productButton);

        JButton DeleteButton = new JButton("Delete Product");
        buttonPanel.add(DeleteButton);


        updateStockButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                String newStockString = JOptionPane.showInputDialog("Enter new stock value:");
                try {
                    int newStock = Integer.parseInt(newStockString);
                    Object productCode = inventoryTable.getValueAt(selectedRow, 0);
                    updateProductStock(productCode, newStock);
                    refreshInventoryTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid stock value entered.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No product selected.");
            }
        });

        productButton.addActionListener(e -> {
            dispose();
            addProduct addProduct = new addProduct();
            addProduct.setVisible(true);
        });

        DeleteButton.addActionListener(e -> {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Product?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Object productCode = inventoryTable.getValueAt(selectedRow, 0);
                    deleteProduct(productCode);
                    refreshInventoryTable();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No product selected.");
            }
        });

        inventoryPanel.add(buttonPanel, BorderLayout.SOUTH);
        return inventoryPanel;
    }
    private void updateProductStock(Object productCode, int newStock) {
        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "UPDATE Product SET Stock = ? WHERE ProductCode = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, newStock);
            pstmt.setObject(2, productCode);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteProduct(Object productCode) {
        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            String query = "DELETE FROM Product WHERE ProductCode = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setObject(1, productCode);
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting product: " + e.getMessage());
        }
    }



    private DefaultTableModel fetchDataFromDatabaseForInventory() {
        DefaultTableModel inventoryModel = new DefaultTableModel();
        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT ProductCode, BrandName, ProductName, FeatureCode, Stock FROM Product");

            inventoryModel.addColumn("Product Code");
            inventoryModel.addColumn("Brand Name");
            inventoryModel.addColumn("Product Name");
            inventoryModel.addColumn("Feature Code");
            inventoryModel.addColumn("Stock");

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
    private void refreshInventoryTable() {
        DefaultTableModel model = fetchDataFromDatabaseForInventory();
        inventoryTable.setModel(model);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JButton ordersButton = new JButton("Orders");
        JButton inventoryButton = new JButton("Inventory");
        JButton archiveButton = new JButton("Order Archive");

        ordersButton.addActionListener(e -> cardLayout.show(mainPanel, "Orders"));
        inventoryButton.addActionListener(e -> cardLayout.show(mainPanel, "Inventory"));
        archiveButton.addActionListener(e -> cardLayout.show(mainPanel, "Archive"));
        sidebar.add(inventoryButton);
        sidebar.add(ordersButton);
        sidebar.add(archiveButton);


        return sidebar;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StaffDashboardScreen().setVisible(true);
        });
    }
    
}
