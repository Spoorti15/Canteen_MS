package canteen;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard extends JFrame implements ActionListener {

    JButton viewBtn, addBtn, updateBtn, transBtn, backBtn; // ✅ declare globally

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(400, 300);
        setLayout(null);

        viewBtn = new JButton("View Inventory");
        viewBtn.setBounds(100, 50, 180, 30);
        add(viewBtn);

        addBtn = new JButton("Add Item");
        addBtn.setBounds(100, 90, 180, 30);
        add(addBtn);

        updateBtn = new JButton("Update Stock");
        updateBtn.setBounds(100, 130, 180, 30);
        add(updateBtn);

        transBtn = new JButton("View Transactions");
        transBtn.setBounds(100, 170, 180, 30);
        add(transBtn);

        // ✅ Back Button
        backBtn = new JButton("Logout");
        backBtn.setBounds(100, 210, 180, 30);
        add(backBtn);

        // Add listeners
        viewBtn.addActionListener(this);
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        transBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        try {
            Connection con = DBConnection.getConnection();

            // 📦 View Inventory
            if (e.getSource() == viewBtn) {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM inventory");

                String data = "ID\tItem\tPrice\tStock\n";
                while (rs.next()) {
                    data += rs.getInt(1) + "\t" +
                            rs.getString(2) + "\t" +
                            rs.getDouble(3) + "\t" +
                            rs.getInt(4) + "\n";
                }
                JOptionPane.showMessageDialog(this, data);
            }

            // ➕ Add Item
            else if (e.getSource() == addBtn) {
                String name = JOptionPane.showInputDialog("Item Name:");
                double price = Double.parseDouble(JOptionPane.showInputDialog("Price:"));
                int stock = Integer.parseInt(JOptionPane.showInputDialog("Stock:"));

                PreparedStatement pst = con.prepareStatement(
                        "INSERT INTO inventory (item_name, price, stock) VALUES (?, ?, ?)"
                );
                pst.setString(1, name);
                pst.setDouble(2, price);
                pst.setInt(3, stock);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Item Added");
            }

            // ➖ Update Stock
            else if (e.getSource() == updateBtn) {
                String name = JOptionPane.showInputDialog("Item Name:");
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Add Stock:"));

                PreparedStatement pst = con.prepareStatement(
                        "UPDATE inventory SET stock = stock + ? WHERE item_name=?"
                );
                pst.setInt(1, qty);
                pst.setString(2, name);
                int rows = pst.executeUpdate();

                if (rows > 0)
                    JOptionPane.showMessageDialog(this, "Stock Updated");
                else
                    JOptionPane.showMessageDialog(this, "Item Not Found");
            }

            // 📊 View Transactions
            else if (e.getSource() == transBtn) {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM transactions");

                String data = "ID\tStudent\tAmount\tDate\n";
                while (rs.next()) {
                    data += rs.getInt(1) + "\t" +
                            rs.getString(2) + "\t" +
                            rs.getDouble(3) + "\t" +
                            rs.getString(4) + "\n";
                }
                JOptionPane.showMessageDialog(this, data);
            }

            // 🔙 Back Button
            else if (e.getSource() == backBtn) {
            	JOptionPane.showMessageDialog(this, "Admin Logged out");
                new LoginPage();
                dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}