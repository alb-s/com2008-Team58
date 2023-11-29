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

        checkoutTableModel = new DefaultTableModel(new Object[]{"ProductCode", "ProductName", "Quantity", "UnitPrice", "LineTotal"}, 0);
        checkoutTable = new JTable(checkoutTableModel);
        checkoutTable.getTableHeader().setReorderingAllowed(false);
        checkoutTable.getTableHeader().setResizingAllowed(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(new JScrollPane(checkoutTable), BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        // TODO: loadOrderLinesFromDatabase();
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout());
        totalAmountLabel = new JLabel("Total Amount: £0.00");
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
        this.dispose(); // Close the current screen
        new CustomerGUI.OrderLineScreen().setVisible(true); // Assuming OrderLineScreen is in the CustomerGUI package
    }
    
    private void performPayment() {
        // TODO: Implement payment logic
        JOptionPane.showMessageDialog(this, "Payment Successful");
    }
    
    // TODO: Implement loadOrderLinesFromDatabase method
    // TODO: Implement any additional methods needed for checkout process

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckoutScreen().setVisible(true));
    }
}

