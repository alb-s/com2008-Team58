package CustomerGUI;

import ManagerGUI.HomeManager;
import StaffGUI.staffView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrderLineScreen extends JFrame {
    private NonEditableTableModel orderLinesTableModel;
    private JTable orderLinesTable;
    

    public OrderLineScreen() {
        setTitle("Order Line");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        JLabel pending = new JLabel("View Pending Orders");

        orderLinesTableModel = new NonEditableTableModel(new String[]{"OrderLineID", "LineNumber", "Quantity", "LineCost", "OrderNumber", "ProductCode", "Status"}, 0);
        orderLinesTable = new JTable(orderLinesTableModel);
        orderLinesTable.getTableHeader().setReorderingAllowed(false);
        orderLinesTable.getTableHeader().setResizingAllowed(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(createOrderLinesTable(), BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(), BorderLayout.SOUTH);
        mainPanel.add(pending,BorderLayout.NORTH);

        add(mainPanel);
        setVisible(true);
        loadOrderLinesFromDatabase();
    }

    private JScrollPane createOrderLinesTable() {
        JScrollPane scrollPane = new JScrollPane(orderLinesTable);
        return scrollPane;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel();


        JPanel bottomPanel = createBottomPanel();
        summaryPanel.add(bottomPanel);

        return summaryPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton historyButton = new JButton("Order History");
        JButton modifyButton = new JButton("Modify Quantity");
        JButton deleteButton = new JButton("Delete Order Line");
        JButton checkoutButton = new JButton("Proceed to Checkout");
        JButton backButton = new JButton("Home");


        historyButton.addActionListener(e -> returnToHistory());
        modifyButton.addActionListener(e -> modifyQuantity());
        deleteButton.addActionListener(e -> deleteOrderLine());
        checkoutButton.addActionListener(e -> proceedCheckout());
        backButton.addActionListener(e -> returnToHome());


        bottomPanel.add(historyButton);
        bottomPanel.add(modifyButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(checkoutButton);
        bottomPanel.add(backButton);


        return bottomPanel;
    }

    private void modifyQuantity() {
        int selectedRow = orderLinesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order line to modify.");
            return;
        }

        String orderLineID = orderLinesTableModel.getValueAt(selectedRow, 0).toString();
        String currentQuantity = orderLinesTableModel.getValueAt(selectedRow, 2).toString();

        String newQuantity = JOptionPane.showInputDialog(this, "Enter new quantity:", currentQuantity);
        if (newQuantity == null || newQuantity.isEmpty()) {
            return; // User cancelled or input empty string
        }

        try {
            int quantity = Integer.parseInt(newQuantity);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
                return;
            }

            updateOrderLineInDatabase(orderLineID, quantity, selectedRow);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void updateOrderLineInDatabase(String orderLineID, int newQuantity, int rowInTable) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String sql = "SELECT RetailPrice FROM Product WHERE ProductCode = (SELECT ProductCode FROM OrderLine WHERE OrderLineID = ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, orderLineID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double unitPrice = rs.getDouble("RetailPrice");
                double newLineCost = unitPrice * newQuantity;

                sql = "UPDATE OrderLine SET Quantity = ?, LineCost = ? WHERE OrderLineID = ?";
                pstmt = connection.prepareStatement(sql);

                pstmt.setInt(1, newQuantity);
                pstmt.setDouble(2, newLineCost);
                pstmt.setString(3, orderLineID);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    orderLinesTableModel.setValueAt(newQuantity, rowInTable, 2); // Update Quantity
                    orderLinesTableModel.setValueAt(newLineCost, rowInTable, 3); // Update Line Cost
                    JOptionPane.showMessageDialog(this, "Order line updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update order line.");
                }
            }

            rs.close();
            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating order line: " + ex.getMessage());
        }
    }
    
    private void deleteOrderLine() {
        int selectedRow = orderLinesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order line to delete.");
            return;
        }

        String orderLineID = orderLinesTableModel.getValueAt(selectedRow, 0).toString();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String sql = "DELETE FROM OrderLine WHERE OrderLineID = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, orderLineID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Order line deleted successfully.");
                orderLinesTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete order line.");
            }

            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting order line: " + ex.getMessage());
        }
    }
    private void proceedCheckout() {
        dispose();
        CheckoutScreen checkoutScreen = new CheckoutScreen();
        checkoutScreen.setVisible(true);

    }
    private void returnToHistory() {
        dispose();
        new OrderLineHistory().setVisible(true);

    }

    private void returnToHome() {
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
            HomeScreen.setVisible(true);
        }
    }

    private void loadOrderLinesFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String userID = Session.getInstance().getUserId();
            String query = "SELECT * FROM OrderLine WHERE userID = ? AND Status = ?";

            try(PreparedStatement pstmt = conn.prepareStatement(query)){
                pstmt.setString(1, userID);
                pstmt.setString(2, "Pending");
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("OrderLineID"),
                        rs.getInt("LineNumber"),
                        rs.getInt("Quantity"),
                        rs.getDouble("LineCost"),
                        rs.getInt("OrderNumber"),
                        rs.getString("ProductCode"),
                        rs.getString("Status"),
                    };
                    orderLinesTableModel.addRow(row);
                }
                rs.close();

            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order lines: " + e.getMessage());
        }
    }

    class NonEditableTableModel extends DefaultTableModel {
        NonEditableTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderLineScreen().setVisible(true));
    }
}
