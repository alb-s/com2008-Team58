package CustomerGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrderLineView extends JFrame {
    private NonEditableTableModel orderLinesTableModel;
    public OrderLineView() {
        setTitle("Order Line");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createOrderInfoPanel(), BorderLayout.NORTH);
        add(createOrderLinesTable(), BorderLayout.CENTER);
        add(createSummaryPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createOrderInfoPanel() {
        JPanel orderInfoPanel = new JPanel(new GridLayout(0, 1));
        orderInfoPanel.add(new JLabel("Order Number: 12345"));
        orderInfoPanel.add(new JLabel("Date: 2023-03-15"));
        orderInfoPanel.add(new JLabel("Customer Name: John Doe"));
        return orderInfoPanel;
    }

    private JScrollPane createOrderLinesTable() {
        String[] columnNames = {"Product Code", "Brand", "Product Name", "Quantity", "Line Cost"};
        orderLinesTableModel = new NonEditableTableModel(columnNames, 0);
        JTable orderLinesTable = new JTable(orderLinesTableModel);
        orderLinesTable.getTableHeader().setReorderingAllowed(false);
        orderLinesTable.getTableHeader().setResizingAllowed(false);

        addDataToTable(); // Add sample data

        return new JScrollPane(orderLinesTable);
    }

    //Dummy
    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel();
        summaryPanel.add(new JLabel("Total Cost: £100"));
        summaryPanel.add(new JLabel("Status: Pending"));
        
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

    // Sample methods for button actions
    private void modifyQuantity() { /* ... */ }
    private void deleteOrderLine() { /* ... */ }
    private void confirmOrder() { /* ... */ }
    private void returnToHome() {
        dispose();
        new Home().setVisible(true);
    }

    private void addDataToTable() {
        orderLinesTableModel.addRow(new Object[]{"R607", "Hornby", "2nd Radius Double Curve", 8, "£32"});
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
        new OrderLineView();
    }
}
    

