package canteen;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class OrderPage extends JFrame implements ActionListener {

    JCheckBox idli, coffee;
    JSpinner q1, q2;
    JLabel totalLabel;
    JButton orderBtn, backBtn;

    double total = 0;
    String studentId; // ✅ store reg no

    public OrderPage(String studentId) {

        this.studentId = studentId;

        setTitle("Order Page");
        setSize(400, 350);
        setLayout(null);

        idli = new JCheckBox("Idli (20)");
        idli.setBounds(30, 30, 120, 25);
        add(idli);

        q1 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        q1.setBounds(180, 30, 50, 25);
        add(q1);

        coffee = new JCheckBox("Coffee (15)");
        coffee.setBounds(30, 70, 120, 25);
        add(coffee);

        q2 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        q2.setBounds(180, 70, 50, 25);
        add(q2);

        totalLabel = new JLabel("Total: 0");
        totalLabel.setBounds(30, 120, 200, 25);
        add(totalLabel);

        orderBtn = new JButton("Place Order");
        orderBtn.setBounds(80, 160, 150, 30);
        add(orderBtn);

        backBtn = new JButton("Logout");
        backBtn.setBounds(80, 200, 150, 30);
        add(backBtn);

        idli.addActionListener(e -> updateTotal());
        coffee.addActionListener(e -> updateTotal());

        orderBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    void updateTotal() {
        total = 0;

        if (idli.isSelected())
            total += 20 * (int) q1.getValue();

        if (coffee.isSelected())
            total += 15 * (int) q2.getValue();

        totalLabel.setText("Total: " + total);
    }

    public void actionPerformed(ActionEvent e) {

        // 🔙 BACK
        if (e.getSource() == backBtn) {
        	JOptionPane.showMessageDialog(this, "Logged out successfully");
            new LoginPage();
            dispose();
            return;
        }

        // 🧾 PLACE ORDER
        if (e.getSource() == orderBtn) {

            int idliQty = (int) q1.getValue();
            int coffeeQty = (int) q2.getValue();

            double tax = total * 0.05;
            double finalTotal = total + tax;

            // 📄 RECEIPT
            String receipt = "------ RECEIPT ------\n";
            receipt += "Student ID: " + studentId + "\n\n";

            if (idli.isSelected())
                receipt += "Idli x " + idliQty + " = " + (idliQty * 20) + "\n";

            if (coffee.isSelected())
                receipt += "Coffee x " + coffeeQty + " = " + (coffeeQty * 15) + "\n";

            receipt += "\nTotal = " + total;
            receipt += "\nTax (5%) = " + tax;
            receipt += "\nFinal = " + finalTotal;

            JOptionPane.showMessageDialog(this, receipt);

            try {
                Connection con = DBConnection.getConnection();

                // 🔴 STOCK CHECK
                if (idli.isSelected()) {
                    PreparedStatement check = con.prepareStatement(
                        "SELECT stock FROM inventory WHERE item_name='Idli'"
                    );
                    ResultSet rs = check.executeQuery();
                    if (rs.next() && rs.getInt(1) < idliQty) {
                        JOptionPane.showMessageDialog(this, "Idli Out of Stock!");
                        return;
                    }
                }

                if (coffee.isSelected()) {
                    PreparedStatement check = con.prepareStatement(
                        "SELECT stock FROM inventory WHERE item_name='Coffee'"
                    );
                    ResultSet rs = check.executeQuery();
                    if (rs.next() && rs.getInt(1) < coffeeQty) {
                        JOptionPane.showMessageDialog(this, "Coffee Out of Stock!");
                        return;
                    }
                }

                // 🟢 SAVE TRANSACTION
                PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO transactions (student_id, total_amount) VALUES (?, ?)"
                );
                pst.setString(1, studentId); // ✅ store reg no
                pst.setDouble(2, finalTotal);
                pst.executeUpdate();

                // 🔽 UPDATE STOCK
                if (idli.isSelected()) {
                    PreparedStatement pst2 = con.prepareStatement(
                        "UPDATE inventory SET stock = stock - ? WHERE item_name='Idli'"
                    );
                    pst2.setInt(1, idliQty);
                    pst2.executeUpdate();
                }

                if (coffee.isSelected()) {
                    PreparedStatement pst3 = con.prepareStatement(
                        "UPDATE inventory SET stock = stock - ? WHERE item_name='Coffee'"
                    );
                    pst3.setInt(1, coffeeQty);
                    pst3.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Order Placed Successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}