package CustomerGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import java.util.Date;

public class HomeScreen extends JFrame {
    private JButton searchButton, EditButton, CardButton, orderButton, outButton, placeButton;
    private JTextField StatsField, searchField, quantityField;
    private JTable table;
    private JComboBox<String> categoryComboBox;

    private Vector<String> columnNames;
    public HomeScreen() {
        setTitle("Home Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel3 = new JPanel();
        placeComponents3(panel3);
        add(panel3);
        setLocationRelativeTo(null);
    }

    private void placeComponents3(JPanel panel3) {
        panel3.setLayout(null);

        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(275, 35, 400, 25);
        Font labelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 26));
        panel3.add(titleLabel);

        JLabel Searchlabel = new JLabel("Search");
        Searchlabel.setBounds(75, 95, 400, 25);
        Searchlabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel3.add(Searchlabel);

        JLabel quanntitylabel = new JLabel("Quantity");
        quanntitylabel.setBounds(75, 135, 400, 25);
        quanntitylabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel3.add(quanntitylabel);

        searchField = new JTextField(20);
        searchField.setBounds(145, 95, 165, 25);
        panel3.add(searchField);

        quantityField = new JTextField(20);
        quantityField.setBounds(145, 135, 165, 25);
        panel3.add(quantityField);

        StatsField = new JTextField("Pending");
        StatsField.setBounds(145, 135, 165, 25);
        StatsField.setEditable(false);
        panel3.add(StatsField);

        searchButton = new JButton("Search");
        searchButton.setBounds(320, 95, 85, 25);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
        panel3.add(searchButton);

        orderButton = new JButton("Order");
        orderButton.setBounds(320, 135, 85, 25);
        orderButton.addActionListener(e -> placeOrder());
        panel3.add(orderButton);

        EditButton = new JButton("Edit Details");
        EditButton.setBounds(0, 0, 100, 25);
        EditButton.addActionListener(e -> performEdit());
        panel3.add(EditButton);

        CardButton = new JButton("Edit Card Details");
        CardButton.setBounds(105, 0, 130, 25);
        CardButton.addActionListener(e -> performCard());
        panel3.add(CardButton);

        outButton = new JButton("Sign Out");
        outButton.setBounds(690, 0, 90, 25);
        outButton.addActionListener(e -> dologin());
        panel3.add(outButton);

        placeButton = new JButton("View Orders");
        placeButton.setBounds(240,0,110,25);
        placeButton.addActionListener(e -> doButton());
        panel3.add(placeButton);

        categoryComboBox = new JComboBox<>(new String[] {"train sets", "track packs", "locomotives", "track", "controllers"}); // Add all relevant categories
        categoryComboBox.setEditable(true);
        categoryComboBox.setBounds(145, 165, 165, 25);
        panel3.add(categoryComboBox);

        JButton ComboButton = new JButton("Search Category");
        ComboButton.setBounds(320, 165, 135, 25);
        panel3.add(ComboButton);

        categoryComboBox.addActionListener(e -> {

            String input = (String) categoryComboBox.getEditor().getItem();
            String category = determineCategory(input);
            updateTableWithCategory(category);
        });

        columnNames = new Vector<>();
        columnNames.add("ProductCode");
        columnNames.add("BrandName");
        columnNames.add("ProductName");
        columnNames.add("RetailPrice");
        columnNames.add("FeatureCode");
        columnNames.add("Gauge");
        columnNames.add("Era");
        columnNames.add("Stock");
        table = new JTable();
        updateTable(fetchDataFromDatabase());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(45, 250, 725, 300);
        panel3.add(scrollPane);
    }
    private void doButton(){
        dispose();
        new OrderLineScreen().setVisible(true);
    }
    private void dologin(){
        dispose();
        LoginScreen Login = new LoginScreen();
        Login.setVisible(true);
    }
    private void performEdit() {
        dispose();
        new UserDetailsScreen().setVisible(true);
    }
    private void performCard() {
        dispose();
        new EditBankDetailsScreen().setVisible(true);
    }
    private void updateTableWithCategory(String category) {
        Vector<Vector<Object>> categoryData = new Vector<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058",
                    "team058", "eel7Ahsi0");
            String sql = "SELECT * FROM Product WHERE ProductCode LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category + "%");
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
                categoryData.add(row);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateTable(categoryData);
    }
    private String determineCategory(String input) {
        if (input.equalsIgnoreCase("train sets")) {
            return "M";
        }
        if (input.equalsIgnoreCase("track packs")) {
            return "P";
        }
        if (input.equalsIgnoreCase("locomotives")) {
            return "L";
        }
        if (input.equalsIgnoreCase("controllers")) {
            return "C";
        }
        if (input.equalsIgnoreCase("waggon")) {
        return "W";
        }
        if (input.equalsIgnoreCase("track")) {
            return "R";
        }
        return input;
    }
    private void placeOrder() {
        String productCode = searchField.getText();
        String quantity = quantityField.getText();
        String Status = StatsField.getText();
        String userID = Session.getInstance().getUserId();
        String userEmail = Session.getInstance().getUserEmail();
        String userPost = Session.getInstance().getPostcode();
        int userNumber = Session.getInstance().getHouseNumber();

        if(quantity.isEmpty() || productCode.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter both a product code and quantity.");
            return;
        }
        int orderQuantity;

        try{
            orderQuantity = Integer.parseInt(quantity);
            if (orderQuantity <= 0){
                JOptionPane.showMessageDialog(null, "Please enter a valid order quantity.");
                return;
            }
        }

        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Please enter a valid order quantity.");
            return;
        }

        try {
            String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
            String dbUsername = "team058";
            String dbPassword = "eel7Ahsi0";
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            PreparedStatement checkProductStmt = connection.prepareStatement("SELECT * FROM Product WHERE ProductCode = ?");
            checkProductStmt.setString(1, productCode);
            ResultSet productResultSet = checkProductStmt.executeQuery();

            if (!productResultSet.next()) {
                JOptionPane.showMessageDialog(null, "Product code does not exist.");
                return;
            }

            double unitPrice = productResultSet.getDouble("RetailPrice");
            double totalCost = unitPrice * orderQuantity;

            Date orderDate = new Date();
            Timestamp orderTimestamp = new Timestamp(orderDate.getTime());

            PreparedStatement insertOrderStmt = connection.prepareStatement("INSERT INTO `OrderLine` (ProductCode, Quantity, LineCost, Status, userID, order_date, housenumber, postcode, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertOrderStmt.setString(1, productCode);
            insertOrderStmt.setInt(2, orderQuantity);
            insertOrderStmt.setDouble(3, totalCost);
            insertOrderStmt.setString(4, Status);
            insertOrderStmt.setString(5,userID);
            insertOrderStmt.setTimestamp(6, orderTimestamp);
            insertOrderStmt.setInt(7, userNumber);
            insertOrderStmt.setString(8,userPost);
            insertOrderStmt.setString(9,userEmail);
            int rowsAffected = insertOrderStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Order placed successfully.\nTotal Cost: " + totalCost);
            }

            else {
                JOptionPane.showMessageDialog(null, "Failed to place order.");
            }

            productResultSet.close();
            checkProductStmt.close();
            insertOrderStmt.close();
            connection.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error processing order.");
        }
    }


    private void performSearch(String searchText) {
        Vector<Vector<Object>> searchData = new Vector<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058",
                    "team058", "eel7Ahsi0");
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058",
                    "team058", "eel7Ahsi0");
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
                row.add(rs.getObject("Stock"));
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
            HomeScreen Home = new HomeScreen();
            Home.setVisible(true);
        });
    }
}
