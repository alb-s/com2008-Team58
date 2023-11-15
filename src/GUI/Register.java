package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Register() {
        setTitle("Registration Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel2 = new JPanel();
        placeComponents2(panel2);
        add(panel2);

        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void placeComponents2(JPanel panel2) {
        panel2.setLayout(null);

        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(180, 35, 140, 25);
        panel2.add(titleLabel);

        JLabel userLabel = new JLabel("name:");
        userLabel.setBounds(150, 100, 80, 25);
        panel2.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(155, 135, 165, 25);
        panel2.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(150, 165, 80, 25);
        panel2.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(155, 195, 165, 25);
        panel2.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(250, 250, 110, 25);
        panel2.add(loginButton);

        registerButton = new JButton("Sign Up");
        registerButton.setBounds(125, 250, 110, 25);
        panel2.add(registerButton);

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
                performregister();
            }
        });
    }

    private void performLogin() {
        dispose();
        Login Login = new Login();
        Login.setVisible(true);
    }


    private void performregister() {
/*put code for registartion here*/

    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Register Register = new Register();
            Register.setVisible(true);
        });
    }
}