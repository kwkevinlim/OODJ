import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class addNewUser implements ActionListener {
    private JFrame frame = new JFrame();
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JComboBox<String> roleComboBox = new JComboBox<>();
    private JButton registerButton = new JButton("Register");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel messageLabel = new JLabel("New User Registration");
    private JLabel roleLabel = new JLabel("User Role:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JLabel usernameLabel = new JLabel("Username:");

    public addNewUser() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        messageLabel.setBounds(50, 20, 300, 30);
        usernameLabel.setBounds(50, 70, 80, 25);
        usernameField.setBounds(140, 70, 200, 25);
        passwordLabel.setBounds(50, 110, 80, 25);
        passwordField.setBounds(140, 110, 200, 25);
        roleLabel.setBounds(50, 150, 80, 25);
        registerButton.setBounds(100, 200, 100, 30);
        cancelButton.setBounds(210, 200, 100, 30);
        roleComboBox.setBounds(140, 150, 200, 25);

        roleComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Select Role", "Admin", "Staff"}));

        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerButton.setBackground(new Color(0, 180, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(this);
        registerButton.setFocusable(false);
        cancelButton.setBackground(new Color(255, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);

        frame.add(messageLabel);
        frame.add(registerButton);
        frame.add(cancelButton);
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(roleLabel);
        frame.add(roleComboBox);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (username.contains(" ") || password.contains(" ")){
                JOptionPane.showMessageDialog(frame,"Username/Password cannot contain spaces.");
                return;
            } else if (username.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(frame,"Both fields must be filled out.");
                return;
            }
            if (role.equals("Select Role")) {
                JOptionPane.showMessageDialog(null, "Please select a role.");
                return;
            }
            if (!UserUtils.userExists(username)) {
                UserUtils.saveUser(username, password, role, frame);
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Username already in use, please enter another username.");
            }
            
        } else if (e.getSource() == cancelButton) {
            new accountManagement();
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        new addNewUser();
    }
}