package CustomerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDetailsView extends JFrame {
    private JTextField oldEmailField = new JTextField(15), 
                       newEmailField = new JTextField(15), 
                       forenameField = new JTextField(15), 
                       surnameField = new JTextField(15),
                       houseNumberField = new JTextField(15), 
                       roadNameField = new JTextField(15), 
                       cityNameField = new JTextField(15), 
                       postcodeField = new JTextField(15);

    public UserDetailsView() {
        setTitle("Edit Details");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        add(createCustomerDetailsPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.PAGE_END);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private JPanel createCustomerDetailsPanel() {
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        detailsPanel.add(createDetailSection("Personal Details", new String[]{ "Old Email","New Email","Forename", "Surname"}));
        detailsPanel.add(createDetailSection("Address Details", new String[]{"House Number", "Road Name", "City Name", "Postcode"}));
        return detailsPanel;
    }
    private JPanel createDetailSection(String title, String[] fields) {
        JPanel sectionPanel = new JPanel(new GridBagLayout());
        sectionPanel.setBorder(BorderFactory.createTitledBorder(title));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);
    
        for (String field : fields) {
            sectionPanel.add(new JLabel(field + ":"), gbc);
            JTextField textField = new JTextField(15);
            sectionPanel.add(textField, gbc); // Correctly add the initialized textField
            switch (field) {
                case "Old Email": oldEmailField = textField; break;
                case "New Email": newEmailField = textField; break;
                case "Forename": forenameField = textField; break;
                case "Surname": surnameField = textField; break;
                case "House Number": houseNumberField = textField; break;
                case "Road Name": roadNameField = textField; break;
                case "City Name": cityNameField = textField; break;
                case "Postcode": postcodeField = textField; break;
            }
        }
        return sectionPanel;
    }
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        JButton editBankDetailsButton = new JButton("Edit Bank Details");
        JButton editPasswordButton = new JButton("Edit Password"); 
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        editBankDetailsButton.addActionListener((ActionEvent e) -> {
            dispose();
            new EditBankDetails().setVisible(true);
        });
    
        editPasswordButton.addActionListener((ActionEvent e) -> {
            new EditPassword().setVisible(true);
        });
    
        saveButton.addActionListener((ActionEvent e) -> {
            saveUserDetails();
        });
    
        cancelButton.addActionListener((ActionEvent e) -> {
            dispose();
            new Home().setVisible(true);
        });
    
        controlPanel.add(saveButton);
        controlPanel.add(cancelButton);
        controlPanel.add(editBankDetailsButton);
        controlPanel.add(editPasswordButton); 
    
        return controlPanel;
    }
    

    private void saveUserDetails() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://stusql.dcs.shef.ac.uk/team058", "team058", "eel7Ahsi0");
            String sql = "UPDATE Users SET email = ?, forename = ?, surname = ?, housenumber = ?, roadname = ?, cityname = ?, postcode = ? WHERE email = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, newEmailField.getText());
            pstmt.setString(2, forenameField.getText());
            pstmt.setString(3, surnameField.getText());
            pstmt.setString(4, houseNumberField.getText());
            pstmt.setString(5, roadNameField.getText());
            pstmt.setString(6, cityNameField.getText());
            pstmt.setString(7, postcodeField.getText());
            pstmt.setString(8, oldEmailField.getText());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Details updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No details updated. Check the old email.");
            }
            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating details: " + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserDetailsView().setVisible(true);
        });
    }
}
