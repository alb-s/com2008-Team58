package CustomerGUI;

import ManagerGUI.HomeManager;
import StaffGUI.staffView;
import Utility.PasswordHashUtility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    static class LoginResult {
        boolean isValid;
        String Role;
        String userId;
        static String userEmail;
        int housenumber;
        String postcode;

        LoginResult(boolean isValid, String Role, String userId, String userEmail, int housenumber, String postcode) {
            this.isValid = isValid;
            this.Role = Role;
            this.userId = userId;
            LoginResult.userEmail = userEmail;
            this.housenumber = housenumber;
            this.postcode = postcode;
        }


    }

    public LoginScreen() {
        setTitle("Login Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        placeComponents(panel);
        add(panel);

        setLocationRelativeTo(null);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(180, 35, 140, 25);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Email:");
        userLabel.setBounds(150, 100, 80, 25);
        panel.add(userLabel);

        emailField = new JTextField(20);
        emailField.setBounds(155, 135, 165, 25);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150, 165, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(155, 195, 165, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(125, 250, 110, 25);
        panel.add(loginButton);

        registerButton = new JButton("Registration");
        registerButton.setBounds(250, 250, 110, 25);
        panel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegister();
            }
        });
    }

    private void performLogin() {
        String username = emailField.getText();
        String password = new String(passwordField.getPassword());
        LoginResult loginResult = checkCredentials(username, password);
    
        if (loginResult.isValid) {
            JOptionPane.showMessageDialog(null, "Login Successful");
    
            // Set session information using LoginResult.userEmail
            Session.getInstance().setUserDetails(loginResult.userId, LoginResult.userEmail, loginResult.Role, loginResult.housenumber, loginResult.postcode);
    
            dispose();
            navigateBasedOnRole(loginResult.Role);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password!");
        }
    }
    


    private void navigateBasedOnRole(String Role) {
        if (Role.equals("Manager")) {
            HomeManager HomeManager = new HomeManager();
            HomeManager.setVisible(true);
        } else if (Role.equals("Customer")) {
            HomeScreen Home = new HomeScreen();
            Home.setVisible(true);
        }
        else{
            staffView staffView = new staffView();
            staffView.setVisible(true);
        }
    }

    private void performRegister() {
        dispose();
        // Assuming Register is another JFrame for registration
        RegisterScreen register = new RegisterScreen();
        register.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginScreen login = new LoginScreen();
            login.setVisible(true);
        });
    }

private LoginResult checkCredentials(String username, String password) {
    DatabaseConnectionHandler connectionHandler = new DatabaseConnectionHandler();

    boolean loginValidator = false;
    String role = null;
    String userId = null;
    String userEmail = null;
    int housenumber = 0;
    String postcode = null;

    try {
        connectionHandler.openConnection();
        Connection connection = connectionHandler.getConnection();

        String query = "SELECT password, Salt, Role, idnew_table, email, housenumber, postcode FROM Users WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("password");
                String storedSalt = resultSet.getString("Salt");

                char[] passwordChars = password.toCharArray();
                String hashedEnteredPassword = PasswordHashUtility.hashPassword(passwordChars, storedSalt);

                if (hashedEnteredPassword != null && hashedEnteredPassword.equals(hashedPasswordFromDB)) {
                    loginValidator = true;
                    role = resultSet.getString("Role");
                    userId = resultSet.getString("idnew_table");
                    userEmail = resultSet.getString("email");
                    housenumber = resultSet.getInt("housenumber");
                    postcode = resultSet.getString("postcode");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        connectionHandler.closeConnection();
    }

    // Return LoginResult object with all fields set
    return new LoginResult(loginValidator, role, userId, userEmail, housenumber, postcode);
}
}



