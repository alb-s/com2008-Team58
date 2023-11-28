package CustomerGUI;

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

        orderLinesTableModel = new NonEditableTableModel(new String[]{"OrderLineID", "LineNumber", "Quantity", "LineCost", "OrderNumber", "ProductCode", "Status"}, 0);
        orderLinesTable = new JTable(orderLinesTableModel);
        orderLinesTable.getTableHeader().setReorderingAllowed(false);
        orderLinesTable.getTableHeader().setResizingAllowed(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(createOrderLinesTable(), BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(), BorderLayout.SOUTH);

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
        summaryPanel.add(new JLabel("Total Cost: £100")); // Replace with actual calculation
        summaryPanel.add(new JLabel("Status: Pending")); // Replace with actual status

        JPanel bottomPanel = createBottomPanel();
        summaryPanel.add(bottomPanel);

        return summaryPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton modifyButton = new JButton("Modify Quantity");
        JButton deleteButton = new JButton("Delete Order Line");
        JButton confirmButton = new JButton("Confirm Order");
        JButton backButton = new JButton("Home");

        modifyButton.addActionListener(e -> modifyQuantity());
        deleteButton.addActionListener(e -> deleteOrderLine());
        confirmButton.addActionListener(e -> confirmOrder());
        backButton.addActionListener(e -> returnToHome());

        bottomPanel.add(modifyButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(confirmButton);
        bottomPanel.add(backButton);

        return bottomPanel;
    }

    private void modifyQuantity() {
        int selectedRow = orderLinesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String newQuantity = JOptionPane.showInputDialog(this, "Enter new quantity:");
            if (newQuantity != null && !newQuantity.isEmpty()) {
                orderLinesTableModel.setValueAt(newQuantity, selectedRow, 3); // Update quantity in the table
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order line to modify.");
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
    
    private void confirmOrder() { 
        JOptionPane.showMessageDialog(this, "Order confirmed."); // Placeholder confirmation message
    } 
    
    private void returnToHome() {
        dispose();
        new HomeScreen().setVisible(true);
    }

    private void loadOrderLinesFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM OrderLine");

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("OrderLineID"),
                    rs.getInt("LineNumber"),
                    rs.getInt("Quantity"),
                    rs.getDouble("LineCost"),
                    rs.getInt("OrderNumber"),
                    rs.getString("ProductCode"),
                    rs.getString("Status")
                };
                orderLinesTableModel.addRow(row);
            }
            rs.close();
            stmt.close();
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
