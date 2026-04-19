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
    private JTextField userIDField = new JTextField();
    private JPasswordField userPasswordField = new JPasswordField();
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel userPasswordLabel = new JLabel("Password: ");
    private JLabel messageLabel = new JLabel("APU Car Service Center Login/Sign up Page");
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
        userIDField.setBounds(125,100,200,25);
        userPasswordField.setBounds(125,150,200,25);

        //Interactive buttons
        messageLabel.setBounds(30,25,350,35);
        messageLabel.setFont(new Font(null,Font.BOLD,18));
        loginButton.setBounds(75,200,100,25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        loginButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setBounds(275,200,100,25);
        resetButton.addActionListener(this);
        resetButton.setFocusable(false);
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setForeground(new Color(249, 94, 61));
        signupButton.setBounds(175,200,100,25);
        signupButton.setFocusable(false);
        signupButton.addActionListener(this);
        signupButton.setBackground(Color.LIGHT_GRAY);
        messageLabel2.setBounds(45,240,400,25);

        frame.add(messageLabel);
        frame.add(usernameLabel);
        frame.add(userPasswordLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(signupButton);
        frame.add(messageLabel2);

    }

    //BUTTON INTERACTIONS
    @Override
    public void actionPerformed(ActionEvent e) {
        String userID = userIDField.getText();
        String userPassword = String.valueOf(userPasswordField.getPassword());
        UserUtils.checkFilePath();

        //RESET
        if(e.getSource()==resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
        }

        //SIGNUP
        if (e.getSource()==signupButton) {
            if (userID.isEmpty() || userPassword.isEmpty()) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Please make sure both fields are filled out.");
                return;
            }
            //to check if spaces in username will affect the program (2)
            if (userID.contains(" ")) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Username cannot include spaces.");
                return;
            }
            if (UserUtils.userExists(userID)) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Username is already in use, please enter a different username.");
            } else{
                    JOptionPane.showMessageDialog(frame, "Account details are available, please select your role.", "Account creation complete.", JOptionPane.INFORMATION_MESSAGE);
                    new AccountSignUp(userID, userPassword);
                    userIDField.setText("");
                    userPasswordField.setText("");
            }
}



        //LOGIN
        if (e.getSource()==loginButton) {
            if (userID.isEmpty() || userPassword.isEmpty()) {
                messageLabel2.setForeground(Color.red);
                messageLabel2.setText("Please make sure both fields have been filled out.");
                return;
            }

            String role = (userLogin(userID, userPassword)); 
            if ("Manager".equals(role)) {
                UserUtils.setSessionRole(role);
                new managerPage();
                frame.dispose();
            }
            else if ("Technician".equals(role)) {
                UserUtils.setSessionRole(role);
                new technicianPage();
                frame.dispose();
            } 
            else if ("Counter Staff".equals(role)) {
                UserUtils.setSessionRole(role);
                new csPage();
                frame.dispose();
            } 
            else if ("Customer".equals(role)) {
                UserUtils.setSessionRole(role);
                new customerPage();
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
                String[] parts = line.split(" | ");
                if (parts.length == 4) {
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

    public static void main(String[] args) {
        UserUtils.newSession();
        new Main();
    }
}
