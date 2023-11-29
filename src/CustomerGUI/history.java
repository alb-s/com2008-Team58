package CustomerGUI;

import javax.swing.*;
import java.awt.*;

public class history extends JFrame {
    public history() {
        setTitle("Order History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new history().setVisible(true));
    }

}