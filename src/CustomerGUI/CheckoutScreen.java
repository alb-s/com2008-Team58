package CustomerGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;

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

        checkoutTableModel = new DefaultTableModel(new Object[]{"OrderLineID", "Quantity", "LineCost", "ProductCode", "Status"}, 0);
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

            double totalCost = 0.0;

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("OrderLineID"),
                        rs.getInt("Quantity"),
                        rs.getDouble("LineCost"),
                        rs.getString("ProductCode"),
                        rs.getString("Status"),
                };
                checkoutTableModel.addRow(row);

                totalCost += rs.getDouble("LineCost");
            }
            rs.close();
            stmt.close();
            conn.close();

            totalAmountLabel.setText("Total Amount: " + totalCost);


        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading order lines: " + e.getMessage());
        }
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout());
        totalAmountLabel = new JLabel();
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
            String productCode = checkoutTableModel.getValueAt(i, 3).toString();
            int orderedQuantity = Integer.parseInt(checkoutTableModel.getValueAt(i, 1).toString());

            int currentStock = getCurrentStock(connection, productCode);
            if (currentStock < orderedQuantity) {
                JOptionPane.showMessageDialog(this, "Insufficient stock for product code: " + productCode);
                connection.rollback();
                return;
            }

            int orderLineID = Integer.parseInt(checkoutTableModel.getValueAt(i, 0).toString());
            updateOrderLineStatus(connection, orderLineID);
        }

        connection.commit();
        JOptionPane.showMessageDialog(this, "Order confirmed.");
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



    private void updateOrderLineStatus(Connection connection, int orderLineID) throws SQLException {
        String sql = "UPDATE OrderLine SET Status = 'Confirmed', order_date = ? WHERE OrderLineID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            Date orderDate = new Date();
            Timestamp orderTimestamp = new Timestamp(orderDate.getTime());
            pstmt.setInt(2, orderLineID);
            pstmt.setTimestamp(1, orderTimestamp);
            pstmt.executeUpdate();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckoutScreen().setVisible(true));
    }
}

