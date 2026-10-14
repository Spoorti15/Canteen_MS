package canteen;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class SignupPage extends JFrame implements ActionListener {

    JTextField regField, userField;
    JPasswordField passField;
    JButton registerBtn, backBtn;

    public SignupPage() {

        setTitle("Signup");
        setSize(320, 300);
        setLayout(null);

        JLabel r = new JLabel("Reg No:");
        r.setBounds(30, 30, 100, 25);
        add(r);

        regField = new JTextField();
        regField.setBounds(120, 30, 140, 25);
        add(regField);

        JLabel u = new JLabel("Username:");
        u.setBounds(30, 70, 100, 25);
        add(u);

        userField = new JTextField();
        userField.setBounds(120, 70, 140, 25);
        add(userField);

        JLabel p = new JLabel("Password:");
        p.setBounds(30, 110, 100, 25);
        add(p);

        passField = new JPasswordField();
        passField.setBounds(120, 110, 140, 25);
        add(passField);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(30, 180, 100, 30);
        add(registerBtn);

        backBtn = new JButton("Cancel");
        backBtn.setBounds(150, 180, 100, 30);
        add(backBtn);

        registerBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == backBtn) {
            new LoginPage();
            dispose();
            return;
        }

        String reg = regField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());

        if (reg.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO students (reg_no, username, password) VALUES (?, ?, ?)"
            );

            pst.setString(1, reg);
            pst.setString(2, user);
            pst.setString(3, pass);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registered Successfully!");
            new LoginPage();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Reg No already exists!");
        }
    }
}