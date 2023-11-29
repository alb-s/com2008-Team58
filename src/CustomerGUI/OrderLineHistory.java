package CustomerGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderLineHistory extends JFrame {

    private NonEditableTableModel orderLinesTableModel;
    private JTable orderLinesTable;

    public OrderLineHistory() {
        setTitle("Order History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

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


        JPanel bottomPanel = createBottomPanel();
        summaryPanel.add(bottomPanel);

        return summaryPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton historyButton = new JButton("Orders");

        historyButton.addActionListener(e -> returnToOrder());

        bottomPanel.add(historyButton);
        return bottomPanel;
    }

    private void returnToOrder(){
        dispose();
        new OrderLineScreen().setVisible(true);
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

    private void loadOrderLinesFromDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String userID = Session.getInstance().getUserId();
            String query = "SELECT * FROM OrderLine WHERE userID = ? AND (Status = ? OR Status = ?)";

            try(PreparedStatement pstmt = conn.prepareStatement(query)){
                pstmt.setString(1, userID);
                pstmt.setString(2, "Fulfiled");
                pstmt.setString(3, "Confirmed");
               
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderLineHistory().setVisible(true));
    }

}