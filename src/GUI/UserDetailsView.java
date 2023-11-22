package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserDetailsView extends JFrame {

    public UserDetailsView() {
        setTitle("Edit Details");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        setLayout(new BorderLayout(10, 10));
        add(createCustomerDetailsPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.PAGE_END);

        // Set a padding around the frame content
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
            sectionPanel.add(new JTextField(15), gbc);
        }

        return sectionPanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        JButton editBankDetailsButton = new JButton("Edit Bank Details");
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        editBankDetailsButton.addActionListener((ActionEvent e) -> {
        // Redirect to edit bank details Screen
        });
   
        saveButton.addActionListener((ActionEvent e) -> {
            // Save button logic
            JOptionPane.showMessageDialog(this, "Details saved successfully.");
        });

        cancelButton.addActionListener((ActionEvent e) -> {
         // Close the frame
        });

        controlPanel.add(saveButton);
        controlPanel.add(cancelButton);
        controlPanel.add(editBankDetailsButton);
        return controlPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserDetailsView().setVisible(true);
        });
    }
}
