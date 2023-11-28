package CustomerGUI;

import Utility.PasswordHashUtility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import CustomerGUI.Register;
import CustomerGUI.Login;



public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Login() {
        setTitle("Login Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        placeComponents(panel);
        add(panel);

        setLocationRelativeTo(null); // Center the frame on the screen
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

        // ActionListener for the login button
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
        boolean isValidUser = checkCredentials(username, password);
        if (isValidUser) {
            JOptionPane.showMessageDialog(null, "Login Successful");
            dispose();
            Home Home = new Home();
            Home.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password!");
        }
    }
    private boolean checkCredentials(String username, String password) {
        String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
        String dbUsername = "team058";
        String dbPassword = "eel7Ahsi0";
        boolean loginValidator = false;
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT password, Salt FROM Users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    String storedSalt = resultSet.getString("Salt");
                        
                    char[] passwordChars = passwordField.getPassword();
                    String hashedEnteredPassword = PasswordHashUtility.hashPassword(passwordChars, storedSalt);
                    
                    if (hashedEnteredPassword != null && hashedEnteredPassword.equals(hashedPasswordFromDB)) {
                        loginValidator = true;
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return loginValidator;
    }

    private void performRegister() {
        dispose();
        Register Register = new Register();
        Register.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login Login = new Login();
            Login.setVisible(true);
        });
    }
}

