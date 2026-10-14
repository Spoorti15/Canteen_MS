package canteen;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {

    JRadioButton adminLogin, userLogin;
    JTextField userField;
    JPasswordField passField;
    JButton loginBtn, signupBtn;

    public LoginPage() {

        setTitle("Login");
        setSize(400, 300);
        setLayout(null);

        adminLogin = new JRadioButton("Admin");
        userLogin = new JRadioButton("User");

        adminLogin.setBounds(80, 30, 100, 25);
        userLogin.setBounds(200, 30, 100, 25);

        ButtonGroup bg = new ButtonGroup();
        bg.add(adminLogin);
        bg.add(userLogin);
        adminLogin.setSelected(true);

        add(adminLogin);
        add(userLogin);

        JLabel u = new JLabel("Username:");
        u.setBounds(50, 80, 100, 25);
        add(u);

        userField = new JTextField();
        userField.setBounds(150, 80, 150, 25);
        add(userField);

        JLabel p = new JLabel("Password:");
        p.setBounds(50, 120, 100, 25);
        add(p);

        passField = new JPasswordField();
        passField.setBounds(150, 120, 150, 25);
        add(passField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(80, 180, 100, 30);
        add(loginBtn);

        signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(200, 180, 100, 30);
        add(signupBtn);

        loginBtn.addActionListener(this);
        signupBtn.addActionListener(this);

        signupBtn.setVisible(false);

        userLogin.addActionListener(e -> signupBtn.setVisible(true));
        adminLogin.addActionListener(e -> signupBtn.setVisible(false));

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        String user = userField.getText();
        String pass = new String(passField.getPassword());

        // ADMIN LOGIN
        if (adminLogin.isSelected()) {
            if (user.equals("admin") && pass.equals("1234")) {
                JOptionPane.showMessageDialog(this, "Admin Login Success");
                new AdminDashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Login");
            }
        }

        // USER LOGIN
        if (userLogin.isSelected()) {
            try {
                Connection con = DBConnection.getConnection();

                PreparedStatement pst = con.prepareStatement(
                    "SELECT * FROM students WHERE username=? AND password=?"
                );

                pst.setString(1, user);
                pst.setString(2, pass);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    String regNo = rs.getString("reg_no"); // ✅ get register number

                    JOptionPane.showMessageDialog(this, "User Login Success");

                    new OrderPage(regNo); // ✅ pass reg no
                    dispose();
          
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid User Login");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // SIGNUP
        if (e.getSource() == signupBtn) {
            new SignupPage();
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}