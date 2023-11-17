package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {

    private JTextField emailField;
    private JTextField foreField;
    private JTextField surField;
    private JTextField roadField;
    private JTextField houseField;
    private JTextField cityField;
    private JTextField postField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public Register() {
        setTitle("Registration Page");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel2 = new JPanel();
        placeComponents2(panel2);
        add(panel2);

        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void placeComponents2(JPanel panel2) {
        panel2.setLayout(null);
        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(275, 35, 400, 25);
        Font labelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 26));
        panel2.add(titleLabel);

        JLabel userLabel = new JLabel("Email:");
        userLabel.setBounds(150, 100, 140, 25);
        panel2.add(userLabel);

        emailField = new JTextField(20);
        emailField.setBounds(155, 135, 140, 25);
        panel2.add(emailField);

        JLabel surLabel = new JLabel("Surname");
        surLabel.setBounds(150, 165, 140, 25);
        panel2.add(surLabel);

        surField = new JTextField(20);
        surField.setBounds(155, 195, 140, 25);
        panel2.add(surField);

        JLabel forLabel = new JLabel("Forename");
        forLabel.setBounds(450, 165, 140, 25);
        panel2.add(forLabel);

        foreField = new JTextField(20);
        foreField.setBounds(455, 195, 140, 25);
        panel2.add(foreField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(450, 100, 80, 25);
        panel2.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(455, 135, 140, 25);
        panel2.add(passwordField);

        JLabel roadLabel = new JLabel("Street Name:");
        roadLabel.setBounds(450, 225, 140, 25);
        panel2.add(roadLabel);

        roadField = new JTextField(20);
        roadField.setBounds(455, 260, 140, 25);
        panel2.add(roadField);

        JLabel houseLabel = new JLabel("House Number:");
        houseLabel.setBounds(150, 225, 140, 25);
        panel2.add(houseLabel);

        houseField = new JTextField(20);
        houseField.setBounds(155, 260, 140, 25);
        panel2.add(houseField);

        JLabel cityLabel = new JLabel("City Name:");
        cityLabel.setBounds(150, 285, 140, 25);
        panel2.add(cityLabel);

        cityField = new JTextField(20);
        cityField.setBounds(155, 320, 140, 25);
        panel2.add(cityField);

        JLabel postLabel = new JLabel("Street Name:");
        postLabel.setBounds(450, 285, 140, 25);
        panel2.add(postLabel);

        postField = new JTextField(20);
        postField.setBounds(455, 320, 140, 25);
        panel2.add(postField);


        loginButton = new JButton("Login");
        loginButton.setBounds(400, 400, 110, 25);
        panel2.add(loginButton);

        registerButton = new JButton("Sign Up");
        registerButton.setBounds(250, 400, 110, 25);
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
        String username = emailField.getText();
        String password = new String(passwordField.getPassword());
    
        
    }
    




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Register Register = new Register();
            Register.setVisible(true);
        });
    }
}