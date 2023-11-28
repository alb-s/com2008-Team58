package ManagerGUI;

import CustomerGUI.LoginScreen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class UserManagement extends JFrame {
    private JButton searchButton, outButton;
    private JButton HomeButton;
    private JButton updateRoleButton;
    private JTextField searchField;
    private JComboBox<String> roleComboBox;
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

        String[] roles = {"Customer", "Staff"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(145, 130, 165, 25);
        panel4.add(roleComboBox);

        // Update Role Button
        updateRoleButton = new JButton("Update Role");
        updateRoleButton.setBounds(320, 130, 150, 25);
        updateRoleButton.addActionListener(e -> updateRole(roleComboBox.getSelectedItem().toString()));
        panel4.add(updateRoleButton);

        HomeButton = new JButton("Home Page");
        HomeButton.setBounds(0, 0, 120, 25);
        HomeButton.addActionListener(e -> performHome());
        panel4.add(HomeButton);

        outButton = new JButton("Sign Out");
        outButton.setBounds(690, 0, 90, 25);
        outButton.addActionListener(e ->  dologin());
        panel4.add(outButton);

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

    private void dologin(){
        dispose();
        LoginScreen Login = new LoginScreen();
        Login.setVisible(true);
    }
    private void performHome() {
        dispose();
        HomeManager HomeManager = new HomeManager();
        HomeManager.setVisible(true);
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
    private void updateRole(String newRole) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) { // Check if a row is selected
            Object userId = table.getModel().getValueAt(selectedRow, 0); // Assuming user ID is in column 0
            if (userId != null) {
                table.getModel().setValueAt(newRole, selectedRow, 10); // Assuming role is in column 10
                updateRoleInDatabase(userId.toString(), newRole);
            } else {
                JOptionPane.showMessageDialog(this, "User ID is null", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    private void updateRoleInDatabase(String userId, String newRole) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String sql = "UPDATE Users SET Role = ? WHERE idnew_table = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newRole);
            pstmt.setString(2, userId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Role updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Role update failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManagement userManagement = new UserManagement();
            userManagement.setVisible(true);
        });
    }
}