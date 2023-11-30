package CustomerGUI;

import Utility.PasswordHashUtility;


import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditPasswordScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField oldPasswordField, newPasswordField;

    public EditPasswordScreen() {
        setTitle("Update Password");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        emailField = new JTextField(20);
        oldPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);

        JPanel passwordDetailsPanel = createPasswordDetailsPanel();
        JPanel buttonPanel = createButtonPanel();

        add(passwordDetailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createPasswordDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(80, 40, 40, 40));

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Old Password:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton submitButton = new JButton("Update Password");
        submitButton.addActionListener(e -> updatePassword());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void updatePassword() {
        String userEmail = emailField.getText();
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
    
        if (userEmail.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }
    
        if (checkOldPassword(userEmail, oldPassword)) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
    
                String selectSaltSql = "SELECT Salt FROM Users WHERE email = ?";
                PreparedStatement selectSaltPstmt = connection.prepareStatement(selectSaltSql);
                selectSaltPstmt.setString(1, userEmail);
                ResultSet saltResultSet = selectSaltPstmt.executeQuery();
    
                //hash new pass with old SALT then store new password
                if (saltResultSet.next()) {
                    String storedSalt = saltResultSet.getString("Salt");
    
                    String hashedNewPassword = PasswordHashUtility.hashPassword(newPassword.toCharArray(), storedSalt);
    
                    String updateSql = "UPDATE Users SET password = ? WHERE email = ?";
                    PreparedStatement updatePstmt = connection.prepareStatement(updateSql);
                    updatePstmt.setString(1, hashedNewPassword);
                    updatePstmt.setString(2, userEmail);
    
                    int affectedRows = updatePstmt.executeUpdate();
    
                    if (affectedRows > 0) {
                        JOptionPane.showMessageDialog(this, "Password updated successfully.");
                        dispose();
                        LoginScreen LoginScreen = new LoginScreen();
                        LoginScreen.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Password update failed.");
                    }
    
                    updatePstmt.close();
                }
    
                selectSaltPstmt.close();
                connection.close();
            } 
            catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } 
        else {
            JOptionPane.showMessageDialog(this, "Check Entered Fields");
        }
    }

    private boolean checkOldPassword(String email, String oldPassword) {

        boolean passwordChange = false;

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
    
            String selectSql = "SELECT password, Salt FROM Users WHERE email = ?";
            PreparedStatement selectPstmt = connection.prepareStatement(selectSql);
            selectPstmt.setString(1, email);
            ResultSet rs = selectPstmt.executeQuery();
    
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String storedSalt = rs.getString("Salt");
    
                String hashedOldPassword = PasswordHashUtility.hashPassword(oldPassword.toCharArray(), storedSalt);
    
                if (hashedOldPassword != null && hashedOldPassword.equals(storedPassword)) {
                    char[] newPasswordChars = newPasswordField.getPassword();
                    String newHashedPassword = PasswordHashUtility.hashPassword(newPasswordChars, storedSalt);
    
                    String updateSql = "UPDATE Users SET password = ? WHERE email = ?";
                    PreparedStatement updatePstmt = connection.prepareStatement(updateSql);
                    updatePstmt.setString(1, newHashedPassword);
                    updatePstmt.setString(2, email);
    
                    int rowsAffected = updatePstmt.executeUpdate();

                    updatePstmt.close();
                    selectPstmt.close();
                    connection.close();

                    return rowsAffected > 0;
                }
            }
    
            // Close resources
            selectPstmt.close();
            connection.close();
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return passwordChange;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditPasswordScreen().setVisible(true));
    }
}
