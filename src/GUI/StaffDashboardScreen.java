package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;

public class StaffDashboardScreen extends JFrame {

    public StaffDashboardScreen() {
        setTitle("Staff Dashboard - Trains of Sheffield");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeUI();
    }

    private JPanel mainPanel;
    private CardLayout cardLayout;

private void initializeUI() {
    mainPanel = new JPanel();
    cardLayout = new CardLayout();
    mainPanel.setLayout(cardLayout);

    // Add different panels here
    // For example:
    mainPanel.add(createOrdersPanel(), "Orders");
    mainPanel.add(createInventoryPanel(), "Inventory");
    mainPanel.add(createUserManagementPanel(), "UserManagement");


    // Add mainPanel to the JFrame
    add(mainPanel, BorderLayout.CENTER);

    // Add a sidebar or menu to switch views
    add(createSidebar(), BorderLayout.WEST);

    
}


private JPanel createOrdersPanel() {
    JPanel ordersPanel = new JPanel(new BorderLayout());
    
    // Form panel
    JPanel formPanel = new JPanel(new GridLayout(0, 2));
    
    // Add form fields
    formPanel.add(new JLabel("Order ID:"));
    formPanel.add(new JTextField(15));
    formPanel.add(new JLabel("Customer Name:"));
    formPanel.add(new JTextField(15));
    formPanel.add(new JLabel("Product:"));
    formPanel.add(new JTextField(15));
    formPanel.add(new JLabel("Quantity:"));
    formPanel.add(new JTextField(15));
    
    // Add a submit button
    JButton submitButton = new JButton("Create Order");
    submitButton.addActionListener(e -> {
        // Add action logic here
    });

    ordersPanel.add(formPanel, BorderLayout.CENTER);
    ordersPanel.add(submitButton, BorderLayout.SOUTH);

    return ordersPanel;
}

private DefaultTableModel userModel;
private DefaultTableModel createUserModel() {
    String[] userColumnNames = {"User ID", "Name", "Role"};
    Object[][] userData = {
        {"001", "John Doe", "Staff"},
        {"002", "Jane Smith", "Manager"},
        // More dummy rows
    };
    userModel = new DefaultTableModel(userData, userColumnNames);
    return userModel;
}

private JPanel createUserManagementPanel() {
    JPanel userManagementPanel = new JPanel(new BorderLayout());

    // Search Panel
    JPanel searchPanel = new JPanel();
    searchPanel.add(new JLabel("Search User:"));
    JTextField searchField = new JTextField(20);
    JButton searchButton = new JButton("Search");
    searchPanel.add(searchField);
    searchPanel.add(searchButton);

   // User Table with the model
   JTable userTable = new JTable(createUserModel());
   JScrollPane userScrollPane = new JScrollPane(userTable);

    // Search Button Action Listener
    searchButton.addActionListener(e -> {
        String searchText = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(userModel);
        userTable.setRowSorter(sorter);

        if (searchText.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Filter based on the second column (Name)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, 1));
        }
    });

    // Action Buttons
    JPanel actionPanel = new JPanel();
    JButton promoteButton = new JButton("Promote to Manager");
    JButton demoteButton = new JButton("Demote to Staff");
    actionPanel.add(promoteButton);
    actionPanel.add(demoteButton);

    // Add components to the panel
    userManagementPanel.add(searchPanel, BorderLayout.NORTH);
    userManagementPanel.add(userScrollPane, BorderLayout.CENTER);
    userManagementPanel.add(actionPanel, BorderLayout.SOUTH);

    JButton userManagerButton = new JButton("User Management");
    userManagerButton.addActionListener(e -> cardLayout.show(mainPanel, "UserManagement"));

    return userManagementPanel;
}


private JPanel createInventoryPanel() {
    JPanel inventoryPanel = new JPanel(new BorderLayout());

    // Dummy data for the table
    String[] columnNames = {"Product ID", "Product Name", "Quantity"};
    Object[][] data = {
        {"001", "Locomotive A", "5"},
        {"002", "Carriage B", "10"},
        // More dummy rows
    };

    JTable inventoryTable = new JTable(data, columnNames);
    JScrollPane scrollPane = new JScrollPane(inventoryTable);
    inventoryPanel.add(scrollPane, BorderLayout.CENTER);
    

    // Add buttons for inventory management
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(new JButton("Add New Item"));
    buttonPanel.add(new JButton("Edit Selected Item"));
    buttonPanel.add(new JButton("Delete Selected Item"));

    inventoryPanel.add(buttonPanel, BorderLayout.SOUTH);

    return inventoryPanel;
}


private JPanel createSidebar() {
    // Create a panel for the sidebar
    JPanel sidebar = new JPanel();
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

    // Create buttons for the sidebar
    JButton ordersButton = new JButton("Orders");
    JButton inventoryButton = new JButton("Inventory");
    JButton userManagerButton = new JButton("User Management");

    // Add action listeners to buttons
    ordersButton.addActionListener(e -> cardLayout.show(mainPanel, "Orders"));
    inventoryButton.addActionListener(e -> cardLayout.show(mainPanel, "Inventory"));
    userManagerButton.addActionListener(e -> cardLayout.show(mainPanel, "UserManagement"));

    // Add buttons to the sidebar
    sidebar.add(ordersButton);
    sidebar.add(inventoryButton);
    sidebar.add(userManagerButton);

    return sidebar;
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StaffDashboardScreen().setVisible(true);
        });
    }
}
