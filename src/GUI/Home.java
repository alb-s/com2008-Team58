package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class Home extends JFrame {
    private JButton searchButton;
    private JButton orderButton;
    private JTextField searchField;
    private JTextField quantityField;
    private JTable table;
    private Vector<String> columnNames;

    public Home() {
        setTitle("Home Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel3 = new JPanel();
        placeComponents3(panel3);
        add(panel3);

        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void placeComponents3(JPanel panel3) {
        panel3.setLayout(null);

        // Title label
        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(275, 35, 400, 25);
        Font labelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 26));
        panel3.add(titleLabel);

        // Search label
        JLabel Searchlabel = new JLabel("Search");
        Searchlabel.setBounds(75, 95, 400, 25);
        Searchlabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel3.add(Searchlabel);

        // Search field
        searchField = new JTextField(20);
        searchField.setBounds(145, 95, 165, 25);
        panel3.add(searchField);

        quantityField = new JTextField(20);
        quantityField.setBounds(145, 135, 165, 25);
        panel3.add(quantityField);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(320, 95, 85, 25);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
        panel3.add(searchButton);

        orderButton = new JButton("Order");
        orderButton.setBounds(320, 135, 85, 25);
        orderButton.addActionListener(e -> placeOrder());
        panel3.add(orderButton);

        // Initialize column names
        columnNames = new Vector<>();
        columnNames.add("ProductCode");
        columnNames.add("BrandName");
        columnNames.add("ProductName");
        columnNames.add("RetailPrice");
        columnNames.add("FeatureCode");
        columnNames.add("Gauge");
        columnNames.add("Era");

        // Initialize and add table
        table = new JTable();
        updateTable(fetchDataFromDatabase());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(45, 250, 725, 300);
        panel3.add(scrollPane);
    }
    private void placeOrder() {
        String productCode = searchField.getText();
        String quantity = quantityField.getText();

        // Perform order processing logic here
        // For example: Insert order details into a database, perform calculations, etc.
        // You can use productCode and quantity variables to process the order

        // For demonstration, display a confirmation message
        JOptionPane.showMessageDialog(null, "Order placed for Product Code: " + productCode + ", Quantity: " + quantity);
    }


    private void performSearch(String searchText) {
        Vector<Vector<Object>> searchData = new Vector<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String sql = "SELECT * FROM Product WHERE ProductCode LIKE ? OR BrandName LIKE ? OR ProductName LIKE ? OR RetailPrice LIKE ? OR FeatureCode LIKE ? OR Gauge LIKE ? OR Era LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            for (int i = 1; i <= 7; i++) { // Repeat for all search columns
                pstmt.setString(i, searchPattern);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getObject("ProductCode"));
                row.add(rs.getObject("BrandName"));
                row.add(rs.getObject("ProductName"));
                row.add(rs.getObject("RetailPrice"));
                row.add(rs.getObject("FeatureCode"));
                row.add(rs.getObject("Gauge"));
                row.add(rs.getObject("Era"));
                searchData.add(row);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTable(searchData);
    }

    private void updateTable(Vector<Vector<Object>> data) {
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
    }

    private static Vector<Vector<Object>> fetchDataFromDatabase() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getObject("ProductCode"));
                row.add(rs.getObject("BrandName"));
                row.add(rs.getObject("ProductName"));
                row.add(rs.getObject("RetailPrice"));
                row.add(rs.getObject("FeatureCode"));
                row.add(rs.getObject("Gauge"));
                row.add(rs.getObject("Era"));
                data.add(row);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Home Home = new Home();
            Home.setVisible(true);
        });
    }
}
