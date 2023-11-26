

package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class UserManagement extends JFrame {
    private JButton searchButton;
    private JTextField searchField;
    private JTable table;
    private Vector<String> columnNames;
    private JLabel messageLabel; // Added message label

    public UserManagement() {
        setTitle("User Management Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel4 = new JPanel();
        placeComponents4(panel4);
        add(panel4);

        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void placeComponents4(JPanel panel4) {
        panel4.setLayout(null);

        // Title label
        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(275, 35, 400, 25);
        Font labelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 26));
        panel4.add(titleLabel);

        // Search label
        JLabel Searchlabel = new JLabel("Search:");
        Searchlabel.setBounds(75, 95, 400, 25);
        Searchlabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 14));
        panel4.add(Searchlabel);

        // Search field
        searchField = new JTextField(20);
        searchField.setBounds(145, 95, 165, 25);
        panel4.add(searchField);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(320, 95, 85, 25);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
        panel4.add(searchButton);

        // Message label setup
        messageLabel = new JLabel("");
        messageLabel.setBounds(45, 225, 725, 20);
        messageLabel.setForeground(Color.RED);
        panel4.add(messageLabel);

        // Initialize column names
        columnNames = new Vector<>();
        columnNames.add("idnew_table");
        columnNames.add("email");
        columnNames.add("password");
        columnNames.add("forename");
        columnNames.add("surname");
        columnNames.add("housenumber");
        columnNames.add("cityname");
        columnNames.add("roadname");
        columnNames.add("postcode");
        columnNames.add("Cardnumber");
        columnNames.add("Role");

        // Initialize and add table
        table = new JTable();
        updateTable(fetchDataFromDatabase());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(45, 250, 725, 300);
        panel4.add(scrollPane);
    }

    private void performSearch(String searchText) {
        messageLabel.setText(""); // Clear previous message
        Vector<Vector<Object>> searchData = new Vector<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String sql = "SELECT * FROM Users WHERE idnew_table LIKE ? OR email LIKE ? OR password LIKE ? OR forename LIKE ? OR surname LIKE ? OR housenumber LIKE ? OR cityname LIKE ? OR roadname LIKE ? OR postcode LIKE ? OR cardNumber LIKE ? OR role LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchText + "%";
            for (int i = 1; i <= 11; i++) { // Repeat for all search columns
                pstmt.setString(i, searchPattern);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getObject("idnew_table"));
                row.add(rs.getObject("email"));
                row.add(rs.getObject("password"));
                row.add(rs.getObject("forename"));
                row.add(rs.getObject("surname"));
                row.add(rs.getObject("housenumber"));
                row.add(rs.getObject("cityname"));
                row.add(rs.getObject("roadname"));
                row.add(rs.getObject("postcode"));
                row.add(rs.getObject("Cardnumber"));
                row.add(rs.getObject("Role"));
                searchData.add(row);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (searchData.isEmpty()) {
            messageLabel.setText("No results found for: " + searchText);
        } else {
            messageLabel.setText("Search results:");
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getObject("idnew_table"));
                row.add(rs.getObject("email"));
                row.add(rs.getObject("password"));
                row.add(rs.getObject("forename"));
                row.add(rs.getObject("surname"));
                row.add(rs.getObject("housenumber"));
                row.add(rs.getObject("cityname"));
                row.add(rs.getObject("roadname"));
                row.add(rs.getObject("postcode"));
                row.add(rs.getObject("Cardnumber"));
                row.add(rs.getObject("Role"));
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
            UserManagement userManagement = new UserManagement();
            userManagement.setVisible(true);
        });
    }
}