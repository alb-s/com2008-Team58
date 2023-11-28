package CustomerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Functions.CardReader;

public class EditBankDetailsScreen extends JFrame {
    public EditBankDetailsScreen() {

        setTitle("Update Bank Details");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel bankDetailsPanel = new JPanel(new GridBagLayout());
        bankDetailsPanel.setBorder(BorderFactory.createTitledBorder("Bank Details"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4); // Padding
        constraints.gridx = 0;
        constraints.gridy = 0;
        addFields(bankDetailsPanel, constraints, new String[]{"Bank Card Name:", "Card Holder Name:",
                "Bank Card Number:", "Card Expiry Date (MM/YY):", "Security Code:"});


        JButton submitBankDetails = new JButton("Update");
        submitBankDetails.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField bankCardNumberField = (JTextField) bankDetailsPanel.getComponent(5);
                CardReader cardReader = new CardReader();
                cardReader.checkCard(bankCardNumberField);
            }
        });


        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomeScreen().setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitBankDetails);
        buttonPanel.add(cancelButton);

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

        new EditBankDetailsScreen();
    }
}
