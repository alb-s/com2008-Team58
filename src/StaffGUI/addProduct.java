package StaffGUI;

import CustomerGUI.LoginScreen;
import CustomerGUI.RegisterScreen;
import Utility.PasswordHashUtility;

import java.security.SecureRandom;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

public class addProduct extends JFrame  {
    private JTextField productCode,BrandName, ProductName, RetailPrice, FeatureCode, Gauge, Era, Stock;


    private JButton addButton;
    private JButton cancelButton;


    public addProduct() {
        setTitle("Add Products");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel11 = new JPanel();
        placeComponents11(panel11);
        add(panel11);

        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void placeComponents11(JPanel panel11) {
        panel11.setLayout(null);
        JLabel titleLabel = new JLabel("Trains of Sheffield");
        titleLabel.setBounds(275, 35, 400, 25);
        Font labelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 26));
        panel11.add(titleLabel);

        JLabel BrandLabel = new JLabel("Brand Name:");
        BrandLabel.setBounds(450, 100, 80, 25);
        panel11.add(BrandLabel);

        BrandName = new JTextField(20);
        BrandName.setBounds(455, 135, 140, 25);
        panel11.add(BrandName);

        JLabel codeLabel = new JLabel("Product Code:");
        codeLabel.setBounds(150, 100, 140, 25);
        panel11.add(codeLabel);

        productCode = new JTextField(20);
        productCode.setBounds(155, 135, 140, 25);
        panel11.add(productCode);

        JLabel productLabel = new JLabel("Product Name:");
        productLabel.setBounds(150, 165, 140, 25);
        panel11.add(productLabel);

        ProductName = new JTextField(20);
        ProductName.setBounds(155, 195, 140, 25);
        panel11.add(ProductName);

        JLabel priceLabel = new JLabel("Retail Price:");
        priceLabel.setBounds(450, 165, 140, 25);
        panel11.add(priceLabel);

        RetailPrice = new JTextField(20);
        RetailPrice.setBounds(455, 195, 140, 25);
        panel11.add(RetailPrice);


        JLabel FeatureLabel = new JLabel("Feature Code:");
        FeatureLabel.setBounds(450, 225, 140, 25);
        panel11.add(FeatureLabel);

        FeatureCode = new JTextField(20);
        FeatureCode.setBounds(455, 260, 140, 25);
        panel11.add(FeatureCode);

        JLabel GaugeLabel = new JLabel("Gauge:");
        GaugeLabel.setBounds(150, 225, 140, 25);
        panel11.add(GaugeLabel);

        Gauge = new JTextField(20);
        Gauge.setBounds(155, 260, 140, 25);
        panel11.add(Gauge);

        JLabel EraLabel = new JLabel("Era:");
        EraLabel.setBounds(150, 285, 140, 25);
        panel11.add(EraLabel);

        Era = new JTextField(20);
        Era.setBounds(155, 320, 140, 25);
        panel11.add(Era);

        JLabel StockLabel = new JLabel("Stock:");
        StockLabel.setBounds(450, 285, 140, 25);
        panel11.add(StockLabel);

        Stock = new JTextField(20);
        Stock.setBounds(455, 320, 140, 25);
        panel11.add(Stock);

        addButton = new JButton("Add Product");
        addButton.setBounds(400, 400, 110, 25);
        panel11.add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(250, 400, 110, 25);
        panel11.add(cancelButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAdd();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performcancel();
            }
        });
    }


    public void performAdd() {
        boolean isValidUser = insertCredentials();
        if (isValidUser) {
            JOptionPane.showMessageDialog(null, "Product Added");
            dispose();
            new StaffDashboardScreen().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Fields!");
        }
    }

    private void performcancel(){
        dispose();
        new StaffDashboardScreen().setVisible(true);
    }

    private boolean insertCredentials (){
        //this code is for accessing database
        String url = "jdbc:mysql://stusql.dcs.shef.ac.uk/team058";
        String dbUsername = "team058";
        String dbPassword = "eel7Ahsi0";
        //this code creates strings we can use for validations later
        String dbcode = productCode.getText();
        String dbBrand = BrandName.getText();
        String dbProduct = ProductName.getText();
        String dbPrice = RetailPrice.getText();
        String dbFeature = FeatureCode.getText();
        String dbGauge = Gauge.getText();
        String dbEra = Era.getText();
        String dbStock = Stock.getText();

        boolean regValidator = false;

        //this try catch block should
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)){
            String query = "INSERT INTO Product (ProductCode,BrandName, ProductName, RetailPrice, FeatureCode, Gauge, Era, Stock) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1,dbcode);
                preparedStatement.setString(2, dbBrand);
                preparedStatement.setString(3, dbProduct);
                preparedStatement.setString(4, dbPrice);
                preparedStatement.setString(5, dbFeature);
                preparedStatement.setString(6, dbGauge);
                preparedStatement.setString(7, dbEra);
                preparedStatement.setString(8, dbStock);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0){
                    regValidator = true;
                }
                else{
                    regValidator = false;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return regValidator;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            addProduct addProduct = new addProduct();
            addProduct.setVisible(true);
        });
    }
}
