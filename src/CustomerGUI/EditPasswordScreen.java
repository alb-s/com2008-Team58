package CustomerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            // String hashedNewPassword = hashPassword(newPassword.toCharArray());

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
                String sql = "UPDATE Users SET password = ? WHERE email = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, userEmail);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Password updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Password update failed.");
                }

                pstmt.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Old password is incorrect.");
        }
    }

    private boolean checkOldPassword(String email, String oldPassword) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String sql = "SELECT password FROM Users WHERE email = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // String hashedOldPassword = hashPassword(oldPassword.toCharArray());
                
                pstmt.close();
                connection.close();
                return oldPassword.equals(storedPassword);
            }
            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // public static String hashPassword(char[] password) {
    //     try {
    //         MessageDigest md = MessageDigest.getInstance("SHA-256");
    //         byte[] hashedBytes = md.digest(new String(password).getBytes());
    //         StringBuilder sb = new StringBuilder();
    //         for (byte b : hashedBytes) {
    //             sb.append(String.format("%02x", b));
    //         }
    //         return sb.toString();
    //     } catch (NoSuchAlgorithmException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditPasswordScreen().setVisible(true));
    }
}
