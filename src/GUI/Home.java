package GUI;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class Home extends JFrame {
    private JButton searchButton;
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
        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(275, 35, 400, 25);
        Font labelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 26));
        panel3.add(titleLabel);

        JLabel Searchlabel = new JLabel("Search");
        Searchlabel.setBounds(75, 95, 400, 25);
        Searchlabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel3.add(Searchlabel);

        JTextField SearchField = new JTextField(20);
        SearchField.setBounds(145, 95, 165, 25);
        panel3.add(SearchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(105, 185, 85, 25);
        panel3.add(searchButton);

        JLabel Quantitylabel = new JLabel("Quantity");
        Quantitylabel.setBounds(75, 145, 400, 25);
        Quantitylabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel3.add(Quantitylabel);

        JTextField QuantityField = new JTextField(20);
        QuantityField.setBounds(145, 145, 165, 25);
        panel3.add(QuantityField);

        Vector<Vector<Object>> data = fetchDataFromDatabase();
        // Create column names
        Vector<String> columnNames = new Vector<>();
        columnNames.add("ProductCode");
        columnNames.add("BrandName");
        columnNames.add("ProductName");
        columnNames.add("RetailPrice");
        columnNames.add("FeatureCode");
        columnNames.add("Gauge");
        columnNames.add("Era");
        // Add more column names based on your data

        // Create JTable with data
        JTable table = new JTable(data, columnNames);
        // Add table to JScrollPane and then to the frame
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(45, 250, 725, 300);
        panel3.add(scrollPane);


    }
    private static Vector<Vector<Object>> fetchDataFromDatabase() {
        Vector<Vector<Object>> data = new Vector<>();

        try {
            // Establish Database Connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");

            // Execute SQL query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

            // Process result set and add to data vector
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getObject("ProductCode"));
                row.add(rs.getObject("BrandName"));
                row.add(rs.getObject("ProductName"));
                row.add(rs.getObject("RetailPrice"));
                row.add(rs.getObject("FeatureCode"));
                row.add(rs.getObject("Gauge"));
                row.add(rs.getObject("Era"));
                // Add more columns as needed
                data.add(row);
            }

            // Close connections
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
