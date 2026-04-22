import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

//general page for all users to edit, different from accountManagement for managers to edit users
public class editProfile implements ActionListener {

    private JFrame frame = new JFrame();
    private JLabel titleLabel = new JLabel("Edit Profile");
    private JButton saveButton = new JButton("Save Changes");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel legalNameLabel = new JLabel("Legal Name: ");
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel passwordLabel = new JLabel("Password: ");
    private JLabel emailLabel = new JLabel("Email: ");
    private JLabel phoneNumberLabel = new JLabel("Phone Number: ");
    private JLabel addressLabel = new JLabel("Address: ");
    private JLabel roleLabel = new JLabel();
    private JLabel dobLabel = new JLabel("Date of Birth: ");
    private JLabel genderLabel = new JLabel("Gender: ");
    private JComboBox<String> genderComboBox = new JComboBox<>(new String[] { "Male", "Female" });
    private JTextField legalNameField = new JTextField();
    private JTextField usernameField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JTextField emailField = new JTextField();
    private JTextField phoneNumberField = new JTextField();
    private JTextField addressField = new JTextField();
    private JTextField dobfield = new JTextField();
    static String role;


    public editProfile() {
        role = userUtilities.getUserRole();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 480);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        titleLabel.setBounds(150, 10, 200, 25);
        legalNameLabel.setBounds(50, 50, 100, 25);
        legalNameField.setBounds(150, 50, 150, 25);
        usernameLabel.setBounds(50, 90, 100, 25);
        usernameField.setBounds(150, 90, 150, 25);
        passwordLabel.setBounds(50, 130, 100, 25);
        passwordField.setBounds(150, 130, 150, 25);
        emailLabel.setBounds(50, 170, 100, 25);
        emailField.setBounds(150, 170, 150, 25);
        phoneNumberLabel.setBounds(50, 210, 100, 25);
        phoneNumberField.setBounds(150, 210, 150, 25);
        addressLabel.setBounds(50, 250, 100, 25);
        addressField.setBounds(150, 250, 150, 25);
        roleLabel.setBounds(50, 330, 200, 25);
        genderComboBox.setBounds(150, 290, 150, 25);
        dobfield.setBounds(150, 370, 150, 25);
        saveButton.setBounds(50, 410, 120, 25);
        cancelButton.setBounds(200, 410, 120, 25);
        genderLabel.setBounds(50, 290, 100, 25);
        dobLabel.setBounds(50, 370, 100, 25);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saveButton.setBackground(new Color(0, 180, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(this);
        saveButton.setFocusable(false);
        cancelButton.setBackground(new Color(255, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);

        frame.add(titleLabel);
        frame.add(legalNameLabel);
        frame.add(legalNameField);
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(phoneNumberLabel);
        frame.add(phoneNumberField);
        frame.add(addressLabel);
        frame.add(addressField);
        frame.add(roleLabel);
        frame.add(genderComboBox);
        frame.add(dobfield);
        frame.add(saveButton);
        frame.add(cancelButton);
        frame.add(genderLabel);
        frame.add(dobLabel);

        displayDetails();
        frame.setVisible(true);
    }

    public void roleShuffler(String role) {
        if (role.equals("Counter Staff")) {
            new csPage();
            frame.dispose();
            // Perform actions based on the role
        } else if (role.equals("Manager")) {
            new managerPage();
            frame.dispose();
        } else if (role.equals("Technician")) {
            // new technicianPage();
            // frame.dispose();
        } else if (role.equals("Customer")) {
            // new customerPage();
            // frame.dispose();
        }
    }

    public String displayDetails() {
        String userID = userUtilities.getUserID();
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                if (line.contains("UserID: " + userID + " |")) {
                    String[] parts = line.split(" \\| ");
                    usernameField.setText(parts[1].split(": ")[1].trim());
                    passwordField.setText(parts[2].split(": ")[1].trim());
                    roleLabel.setText("User Type: " + parts[3].split(": ")[1].trim());
                    legalNameField.setText(parts[4].split(": ")[1].trim());
                    emailField.setText(parts[5].split(": ")[1].trim());
                    phoneNumberField.setText(parts[6].split(": ")[1].trim());
                    addressField.setText(parts[7].split(": ")[1].trim());
                    genderComboBox.setSelectedItem(parts[8].split(": ")[1].trim());
                    dobfield.setText(parts[9].split(": ")[1].trim());
                    return line;
                }
                }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String userName = usernameField.getText();
            String password = passwordField.getText();
            String legalName = legalNameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            String address = addressField.getText();
            String gender = (String) genderComboBox.getSelectedItem();
            String dob = dobfield.getText();
            if (userName.isEmpty() || password.isEmpty() || legalName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || 
                address.isEmpty() || dob.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please make sure all fields have been filled out.");
                return;
            } else {
                String userID = userUtilities.getUserID();
                String existingDetails = userUtilities.getUserDetails(userID);
                String [] parts = existingDetails.split(" \\| ");
                String existingRole = parts[3].split(": ")[1].trim();
                userUtilities.updateUserDetails(userID, userName, password, existingRole, legalName, email, phoneNumber, address, gender, dob);
            }
            roleShuffler(role);
        } else if (e.getSource() == cancelButton) {
            // code to cancel editing and return to previous page
            roleShuffler(role);
        }
    }

    public static void main(String[] args) {
        new editProfile();
    }
}
