package StaffGUI;
import CustomerGUI.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.util.Vector;
import CustomerGUI.LoginScreen;

public class staffView extends JFrame {
    private JButton searchButton, EditButton, CardButton, orderButton, outButton, StaffButton, placeButton;
    private JTextField StatsField, searchField, quantityField;
    private JTable table;
    private Vector<String> columnNames;
    private JComboBox<String> categoryComboBox;
    public staffView() {
        setTitle("Home Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel9 = new JPanel();
        placeComponents9(panel9);
        add(panel9);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void placeComponents9(JPanel panel9) {
        panel9.setLayout(null);

        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(275, 35, 400, 25);
        Font labelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 26));
        panel9.add(titleLabel);

        JLabel Searchlabel = new JLabel("Search");
        Searchlabel.setBounds(75, 95, 400, 25);
        Searchlabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel9.add(Searchlabel);

        JLabel quanntitylabel = new JLabel("Quantity");
        quanntitylabel.setBounds(75, 135, 400, 25);
        quanntitylabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel9.add(quanntitylabel);

        searchField = new JTextField(20);
        searchField.setBounds(145, 95, 165, 25);
        panel9.add(searchField);

        quantityField = new JTextField(20);
        quantityField.setBounds(145, 135, 165, 25);
        panel9.add(quantityField);

        StatsField = new JTextField("Pending");
        StatsField.setBounds(145, 135, 165, 25);
        StatsField.setEditable(false);
        panel9.add(StatsField);

        searchButton = new JButton("Search");
        searchButton.setBounds(320, 95, 85, 25);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
        panel9.add(searchButton);

        orderButton = new JButton("Order");
        orderButton.setBounds(320, 135, 85, 25);
        orderButton.addActionListener(e -> placeOrder());
        panel9.add(orderButton);

        EditButton = new JButton("Edit Details");
        EditButton.setBounds(0, 0, 100, 25);
        EditButton.addActionListener(e -> performEdit());
        panel9.add(EditButton);

        CardButton = new JButton("Edit Card Details");
        CardButton.setBounds(105, 0, 130, 25);
        CardButton.addActionListener(e -> performCard());
        panel9.add(CardButton);

        StaffButton = new JButton("Staff Page");
        StaffButton.setBounds(240, 0, 100, 25);
        StaffButton.addActionListener(e -> performStaff());
        panel9.add(StaffButton);

        outButton = new JButton("Sign Out");
        outButton.setBounds(690, 0, 90, 25);
        outButton.addActionListener(e -> dologin());
        panel9.add(outButton);

        placeButton = new JButton("View Orders");
        placeButton.setBounds(345,0,110,25);
        placeButton.addActionListener(e -> doButton());
        panel9.add(placeButton);

        categoryComboBox = new JComboBox<>(new String[] {"train sets", "track packs", "locomotives", "track", "controllers"}); // Add all relevant categories
        categoryComboBox.setEditable(true);
        categoryComboBox.setBounds(145, 165, 165, 25);
        panel9.add(categoryComboBox);

        JButton ComboButton = new JButton("Search Category");
        ComboButton.setBounds(320, 165, 135, 25);
        panel9.add(ComboButton);

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
        panel9.add(scrollPane);
    }

    private void dologin(){
        dispose();
        LoginScreen Login = new LoginScreen();
        Login.setVisible(true);
    }

    private void doButton(){
        dispose();
        new OrderLineScreen().setVisible(true);
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
    private void performEdit() {
        dispose();
        new UserDetailsScreen().setVisible(true);
    }
    private void performStaff() {
        dispose();
        new StaffDashboardScreen().setVisible(true);
    }
    private void performCard() {
        dispose();
        new EditBankDetailsScreen().setVisible(true);
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

            java.util.Date orderDate = new Date();
            Timestamp orderTimestamp = new Timestamp(orderDate.getTime());

            double unitPrice = productResultSet.getDouble("RetailPrice");
            double totalCost = unitPrice * orderQuantity;


            PreparedStatement insertOrderStmt = connection.prepareStatement("INSERT INTO `OrderLine` (ProductCode, Quantity, LineCost, Status, userID, order_date, housenumber, postcode, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");            insertOrderStmt.setString(1, productCode);
            insertOrderStmt.setInt(2, orderQuantity);
            insertOrderStmt.setDouble(3, totalCost);
            insertOrderStmt.setString(4, Status);
            insertOrderStmt.setString(5,userID);
            insertOrderStmt.setTimestamp(6,orderTimestamp);
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
            staffView staffView = new staffView();
            staffView.setVisible(true);
        });
    }
}
