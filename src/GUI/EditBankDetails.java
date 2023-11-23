package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Functions.CardReader;


public class EditBankDetails extends JFrame {
    public EditBankDetails() {
        setTitle("Update Bank Details");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel for bank details
        JPanel bankDetailsPanel = new JPanel(new GridBagLayout());
        bankDetailsPanel.setBorder(BorderFactory.createTitledBorder("Bank Details"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4); // Padding
        constraints.gridx = 0;
        constraints.gridy = 0;
        addFields(bankDetailsPanel, constraints, new String[]{"Bank Card Name:", "Card Holder Name:", "Bank Card Number:", "Card Expiry Date (MM/YY):", "Security Code:"});

        // Submit button for bank details
        JButton submitBankDetails = new JButton("Update");
        submitBankDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField bankCardNumberField = (JTextField) bankDetailsPanel.getComponent(2);
                CardReader cardReader = new CardReader();
                cardReader.checkCard(bankCardNumberField);
            }
        });

        // Cancel button to go back to main page
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Redirect to home page
                new Home().setVisible(true);
            }
        });

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitBankDetails);
        buttonPanel.add(cancelButton);

        // Adding components to frame
        add(bankDetailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addFields(JPanel panel, GridBagConstraints constraints, String[] labels) {
        for (String label : labels) {
            constraints.gridx = 0;
            panel.add(new JLabel(label), constraints);
            constraints.gridx++;
            panel.add(new JTextField(20), constraints);
            constraints.gridy++;
        }
    }

    public static void main(String[] args) {
        new EditBankDetails();
    }
}
