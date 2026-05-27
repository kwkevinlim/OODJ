import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;


public class Main implements ActionListener{

    private JFrame frame = new JFrame();
    private JButton loginButton = new JButton("Sign In");
    private JButton signupButton = new JButton("Sign Up");
    private JButton resetButton = new JButton("Reset");
    private JTextField usernameField = new JTextField();
    private JPasswordField userPasswordField = new JPasswordField();
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel userPasswordLabel = new JLabel("Password: ");
    private JLabel titleLabel = new JLabel("APU Car Service Center");
    private JLabel titleLabel2 = new JLabel("Management System");
    private JLabel messageLabel2 = new JLabel("");

    //GUI
    public Main() {
        //GUI Body (frame)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,350);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        //User inputs (label = name, field = text field)
        usernameLabel.setBounds(50,100,75,25);
        userPasswordLabel.setBounds(50,150,75,25);
        usernameField.setBounds(125,100,200,25);
        userPasswordField.setBounds(125,150,200,25);

        //Interactive buttons
        titleLabel.setBounds(125,30,200,35);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel2.setBounds(135,55,200,35);
        titleLabel2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBounds(120,185,100,25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        loginButton.setBackground(new Color(0,180,0));
        loginButton.setForeground(Color.WHITE);
        resetButton.setBounds(175,215,100,25);
        resetButton.addActionListener(this);
        resetButton.setFocusable(false);
        resetButton.setBackground(new Color(220, 50, 50));
        resetButton.setForeground(Color.white);
        signupButton.setBounds(230,185,100,25);
        signupButton.setFocusable(false);
        signupButton.addActionListener(this);
        signupButton.setBackground(new Color(220, 150,0));
        signupButton.setForeground(Color.WHITE);
        messageLabel2.setBounds(45,240,400,25);

        frame.add(titleLabel);
        frame.add(titleLabel2);
        frame.add(usernameLabel);
        frame.add(userPasswordLabel);
        frame.add(usernameField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(signupButton);
        frame.add(messageLabel2);

    }

    //BUTTON INTERACTIONS
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String userPassword = String.valueOf(userPasswordField.getPassword());
        String legalName = "", email = "", phoneNumber = "", address = "", gender = "", dob = "";
        userUtilities.checkFilePath();

        //RESET
        if(e.getSource()==resetButton) {
            usernameField.setText("");
            userPasswordField.setText("");
        }

        //SIGNUP
        if (e.getSource()==signupButton) {
            if (username.isEmpty() || userPassword.isEmpty()) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Please make sure both fields are filled out.");
                return;
            }
            //prevents file reading erros
            if (username.contains("|")) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Username cannot include '|' characters.");
                return;
            }
            //checks if username is taken
            if (userUtilities.userExists(username)) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Username is already in use, please enter a different username.");
            } else{
            //if all is ok then username and password is added to users.txt, but they need to update their details later in editProfile
                    userUtilities.saveUser(username, userPassword, "Customer", frame, legalName, email, phoneNumber, address, gender, dob);
                    JOptionPane.showMessageDialog(frame, "Account creation successful!", 
                    "Account creation complete.", JOptionPane.INFORMATION_MESSAGE);
                    
                    // new AccountSignUp(userID, userPassword);
                    usernameField.setText("");
                    userPasswordField.setText("");
            }
}



        //LOGIN
        if (e.getSource()==loginButton) {
            if (username.isEmpty() || userPassword.isEmpty()) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Please make sure both fields have been filled out.");
                return;
            }

            String role = (userLogin(username, userPassword)); 
            //have to set access limits based on role
            if ("Manager".equals(role)) {
                userUtilities.setUserRole(role);
                userUtilities.setUserID(getUserID(username));
                new managerPage();
                frame.dispose();
            }
            else if ("Technician".equals(role)) {
                userUtilities.setUserRole(role);
                userUtilities.setUserID(getUserID(username));
                new technicianPage();
                frame.dispose();
            } 
            else if ("Counter Staff".equals(role)) {
                userUtilities.setUserRole(role);
                userUtilities.setUserID(getUserID(username));
                new csPage();
                frame.dispose();
            } 
            else if ("Customer".equals(role)) {
                userUtilities.setUserRole(role);
                userUtilities.setUserID(getUserID(username));
                // new customerPage();
                frame.dispose();
            } 
            else{
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Username/Password does not match, please try again.");
            }
        }
    }

    //SIGN IN PROCESS
    private String userLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length >= 4) {
                    String storedUsername = parts[1].split(": ")[1].trim();
                    String storedPassword = parts[2].split(": ")[1].trim();
                    String storedRole = parts[3].split(": ")[1].trim();
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return storedRole;
                    }
                }
            }
        } catch(IOException ignored) {}
        return null;
    }

    //helper to get userID for first-time login
    private String getUserID(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line=reader.readLine()) != null) {
                String [] parts = line.split(" \\| ");
                if (parts.length >= 2 && parts[1].split(": ")[1].trim().equals(username)) {
                    return parts[0].split(": ")[1].trim();
                }
            } 
        }catch (IOException e) {
                e.printStackTrace();
            } 
            return null;
    }

    public static void main(String[] args) {
        userUtilities.newSession();
        new Main();
    }
}
