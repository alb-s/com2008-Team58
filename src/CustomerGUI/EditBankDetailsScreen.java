package CustomerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Functions.CardReader;
import ManagerGUI.HomeManager;
import StaffGUI.staffView;

public class EditBankDetailsScreen extends JFrame {
    public EditBankDetailsScreen() {

        setTitle("Update Bank Details");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

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
                String email = Session.getInstance().getUserEmail();
                cardReader.checkCard(bankCardNumberField,email);
                String Role = Session.getInstance().getUserRole();
                if (Role.equals("Manager")) {
                    dispose();
                    HomeManager HomeManager = new HomeManager();
                    HomeManager.setVisible(true);
                } else if (Role.equals("Staff")) {
                    dispose();
                    staffView staffView = new staffView();
                    staffView.setVisible(true);
                }
                else{
                    dispose();
                    HomeScreen HomeScreen = new HomeScreen();
                    HomeScreen.setVisible(true);
                }
            }
        });


        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dohome();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(submitBankDetails);
        buttonPanel.add(cancelButton);

        add(bankDetailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void dohome(){
        String Role = Session.getInstance().getUserRole();
        if (Role.equals("Manager")) {
            dispose();
            HomeManager HomeManager = new HomeManager();
            HomeManager.setVisible(true);
        } else if (Role.equals("Staff")) {
            dispose();
            staffView staffView = new staffView();
            staffView.setVisible(true);
        }
        else{
            dispose();
            HomeScreen HomeScreen = new HomeScreen();
            HomeScreen.setVisible(true);
        }

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
