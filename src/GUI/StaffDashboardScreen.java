package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    // Layout for the entire frame
    setLayout(new BorderLayout());
    add(createTopPanel(), BorderLayout.NORTH);
    add(mainPanel, BorderLayout.CENTER);

    // Add a sidebar or menu to switch views
    add(createSidebar(), BorderLayout.WEST);
}

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                new Login().setVisible(true);
            }
        });
        
        JButton homeButton = new JButton("Home");
        signOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                new Home().setVisible(true);
            }
        });

        topPanel.add(signOutButton, BorderLayout.WEST);
        topPanel.add(homeButton, BorderLayout.EAST);
        return topPanel;
    }

private JPanel createOrdersPanel() {
    JPanel orderPanel = new JPanel(new BorderLayout());

    // Table for pending orders
    String[] columnNames = {"Order ID", "Date/Time", "Details"};
    JTable ordersTable = new JTable(new Object[][]{}, columnNames);
    orderPanel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

    // Buttons for order management
    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton fulfillButton = new JButton("Fulfill Order");
    JButton deleteOrderButton = new JButton("Delete Order");
    buttonPanel.add(fulfillButton);
    buttonPanel.add(deleteOrderButton);

    // Add action listeners for buttons

    orderPanel.add(buttonPanel, BorderLayout.SOUTH);

    return orderPanel;
}

private DefaultTableModel userModel;
private DefaultTableModel createUserModel() {
    String[] userColumnNames = {"User ID", "Name", "Role"};
    Object[][] userData = {
        {"001", "John Doe", "Staff"},
        {"002", "Jane Smith", "Manager"},
        // More dummy rows
        // dummy rows
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
    JPanel inventoryPanel = new JPanel();
    inventoryPanel.setLayout(new BorderLayout());

    // Dropdown for product categories
    JComboBox<String> categoryDropdown = new JComboBox<>(new String[]{"Category 1", "Category 2", "Category 3"});
    inventoryPanel.add(categoryDropdown, BorderLayout.NORTH);

    // Dummy data for the table
    String[] columnNames = {"Product ID", "Product Name", "Quantity"};
    Object[][] data = {
        {"001", "Locomotive A", "5"},
        {"002", "Carriage B", "10"},
        // More dummy rows
    };


    DefaultTableModel inventoryModel = new DefaultTableModel(data, columnNames);
    JTable inventoryTable = new JTable(inventoryModel);
    JScrollPane scrollPane = new JScrollPane(inventoryTable);
    inventoryPanel.add(scrollPane, BorderLayout.CENTER);


    // Add buttons for inventory management
    // Buttons for inventory management
    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Add New Item");
    JButton editButton = new JButton("Edit Selected Item");
    JButton deleteButton = new JButton("Delete Selected Item");
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);

    // Add action listeners for buttons

    inventoryPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Add sorting and searching
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(inventoryModel);
    inventoryTable.setRowSorter(sorter);

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
