package CustomerGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class CheckoutScreen extends JFrame {
    private DefaultTableModel checkoutTableModel;
    private JTable checkoutTable;
    private JLabel totalAmountLabel;

    public CheckoutScreen() {
        setTitle("Checkout");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        checkoutTableModel = new DefaultTableModel(new Object[]{"OrderLineID","LineNumber", "Quantity", "LineCost", "OrderNumber", "ProductCode", "Status"}, 0);
        checkoutTable = new JTable(checkoutTableModel);
        checkoutTable.getTableHeader().setReorderingAllowed(false);
        checkoutTable.getTableHeader().setResizingAllowed(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(new JScrollPane(checkoutTable), BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(), BorderLayout.SOUTH);
        loadOrderLinesFromDatabase();
        add(mainPanel);
        setVisible(true);
    }

    private void loadOrderLinesFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            Statement stmt = conn.createStatement();
            String userID = Session.getInstance().getUserId();
            String pending = "pending";
            String query = "SELECT * FROM OrderLine WHERE userID = '" + userID + "' AND Status = '" + pending + "'";
            ResultSet rs = stmt.executeQuery(query);

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
                checkoutTableModel.addRow(row);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order lines: " + e.getMessage());
        }
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout());
        totalAmountLabel = new JLabel("Total Amount:");
        summaryPanel.add(totalAmountLabel, BorderLayout.WEST);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> returnToOrderLineScreen());
        buttonPanel.add(backButton);
    
        JButton payButton = new JButton("Pay");
        payButton.addActionListener(e -> performPayment());
        buttonPanel.add(payButton);
    
        summaryPanel.add(buttonPanel, BorderLayout.EAST);
    
        return summaryPanel;
    }
    
    private void returnToOrderLineScreen() {
        this.dispose();
        new CustomerGUI.OrderLineScreen().setVisible(true);
    }

private void performPayment(){
    Connection connection = null;
    try {
        connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");

        if (!hasValidCardNumber(connection, Session.getInstance().getUserId())) {
            JOptionPane.showMessageDialog(this, "No valid card number found. Redirecting...");
            dispose();
            new EditBankDetailsScreen().setVisible(true);
            return;
        }
        connection.setAutoCommit(false);

        for (int i = 0; i < checkoutTableModel.getRowCount(); i++) {
            String productCode = checkoutTableModel.getValueAt(i, 5).toString();
            int orderedQuantity = Integer.parseInt(checkoutTableModel.getValueAt(i, 2).toString());

            int currentStock = getCurrentStock(connection, productCode);
            if (currentStock < orderedQuantity) {
                JOptionPane.showMessageDialog(this, "Insufficient stock for product code: " + productCode);
                connection.rollback();
                return;
            }

            updateProductStock(connection, productCode, currentStock - orderedQuantity);
            int orderLineID = Integer.parseInt(checkoutTableModel.getValueAt(i, 0).toString());
            updateOrderLineStatus(connection, orderLineID);
        }

        connection.commit();
        JOptionPane.showMessageDialog(this, "Order confirmed and stock updated.");
        dispose();
        new OrderLineScreen().setVisible(true);
    } catch (SQLException e) {
        try {
            if (connection != null) {
                connection.rollback(); // Rollback transaction in case of error
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Error during checkout: " + e.getMessage());
    } finally {
        try {
            if (connection != null) {
                connection.close(); // Always close the connection
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    }

    private boolean hasValidCardNumber(Connection connection, String userID) throws SQLException {
        String sql = "SELECT CardNumber FROM Users WHERE idnew_table = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("CardNumber") != null && !rs.getString("CardNumber").isEmpty();
            }
            return false;
        }
    }
        private int getCurrentStock(Connection connection, String productCode) throws SQLException {
        String sql = "SELECT Stock FROM Product WHERE ProductCode = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Stock");
            } else {
                throw new SQLException("Product not found with code: " + productCode);
            }
        }
    }

    private void updateProductStock(Connection connection, String productCode, int newStock) throws SQLException {
        String sql = "UPDATE Product SET Stock = ? WHERE ProductCode = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newStock);
            pstmt.setString(2, productCode);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating stock failed, no rows affected.");
            }
        }
    }

    private void updateOrderLineStatus(Connection connection, int orderLineID) throws SQLException {
        String sql = "UPDATE OrderLine SET Status = 'Confirmed' WHERE OrderLineID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderLineID);
            pstmt.executeUpdate();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckoutScreen().setVisible(true));
    }
}

