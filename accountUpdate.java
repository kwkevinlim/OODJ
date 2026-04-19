import javax.swing.*;

import java.awt.Color;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class accountUpdate implements ActionListener{

    private JFrame frame = new JFrame();
    private JLabel searchLabel = new JLabel("Search:");
    private JTextField searchField = new JTextField();
    private JLabel userIDLabel = new JLabel("UserID:");
    private JLabel usernameLabel = new JLabel("Username:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JLabel roleLabel = new JLabel("Role:");
    private JTextField usernameField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JLabel userIDLabel2 = new JLabel();
    private JLabel roleLabel2 = new JLabel();
    private JButton searchButton = new JButton("Search");
    private JButton updateButton = new JButton("Update");
    private JButton returnButton = new JButton("Return");
    private JButton managerButton = new JButton("Manager");
    private JButton technicianButton = new JButton("Technician");
    private JButton csButton = new JButton("Counter Staff");
    private JButton customerButton = new JButton("Customer");
    private JButton resetButton = new JButton("Reset");

    private String originalUsername = "";

    public accountUpdate() {

        frame.setTitle("Update User Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650,250);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

		searchLabel.setBounds(20, 20, 50, 25);
		searchField.setBounds(70, 20, 135, 25);
		searchButton.setBounds(220, 20, 80, 25);
		userIDLabel.setBounds(20, 60, 100, 25);
		usernameField.setBounds(90, 110, 180, 25);
		passwordField.setBounds(370, 110, 180, 25);
		roleLabel.setBounds(120, 60, 100, 25);
        resetButton.setBounds(260, 160, 100, 30);
		updateButton.setBounds(380,160, 100, 30);
		returnButton.setBounds(500, 160, 100, 30);
        usernameLabel.setBounds(20, 110, 80, 25);
        passwordLabel.setBounds(300, 110, 80, 25);
        userIDLabel2.setBounds(67,60,60,25);
        roleLabel2.setBounds(155,60,100,25);
        //placeholder sizes, to be adjusted once running
        managerButton.setBounds(260, 60, 80 ,25);
        technicianButton.setBounds(360, 60, 80 ,25);
        csButton.setBounds(260, 60, 80 ,25);
        customerButton.setBounds(360, 60, 80 ,25);

        JButton[] buttons = {searchButton, updateButton, managerButton, 
        technicianButton, csButton, customerButton, resetButton};
        for (JButton button : buttons) {
            button.setFocusable(false);
            button.addActionListener(this);
            button.setBackground(Color.LIGHT_GRAY);
        }

        returnButton.setFocusable(false);
        returnButton.addActionListener(this);
        returnButton.setBackground(Color.red);
        returnButton.setForeground(Color.white);

        frame.add(searchLabel);
		frame.add(searchField);
		frame.add(searchButton);
		frame.add(userIDLabel);
        frame.add(usernameLabel);
        frame.add(passwordLabel);
		frame.add(usernameField);
		frame.add(passwordField);
		frame.add(roleLabel);
		frame.add(updateButton);
		frame.add(returnButton);
        frame.add(resetButton);
        frame.add(roleLabel2);
        frame.add(managerButton);
        frame.add(technicianButton);
        frame.add(csButton);
        frame.add(customerButton);
        frame.add(userIDLabel2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==returnButton) {
            new accountManagement();
            frame.dispose();
        } else if (e.getSource()==resetButton) {
            searchField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            roleLabel2.setText("");
            userIDLabel2.setText("");
        } else if (e.getSource()==managerButton) {
            roleLabel2.setText("Manager");
        } else if (e.getSource()==technicianButton) {
            roleLabel2.setText("Technician");
        } else if (e.getSource()==csButton) {
            roleLabel2.setText("Counter Staff");
        } else if (e.getSource()==customerButton) {
            roleLabel2.setText("Customer");
        }

        else if (e.getSource()==searchButton) {
            try {
                int userID = Integer.parseInt(searchField.getText());
                String line;
                boolean found = false;
                try (BufferedReader br = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
                    while ((line = br.readLine()) != null)  {
                        if (line.contains("UserID: " + userID + " | ")) {
                            String parts[] = line.split(" | ");
                            userIDLabel2.setText(parts[0].split(": ")[1]);
                            usernameField.setText(parts[1].split(": ")[1]);
                            passwordField.setText(parts[2].split(": ")[1]);
                            roleLabel2.setText(parts[3].split(": ")[1]);
                            originalUsername = parts[1].split(": ")[1];
                            found = true;
                            break;
                        }
                    }
                } catch (Exception ex){
                    System.out.println("BR ERROR");
                }
                if (found == false) {
                    JOptionPane.showMessageDialog(frame, "UserID not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid UserID number.");
                searchField.setText("");
            }
        }

        else if (e.getSource()==updateButton) {
            String userID = userIDLabel2.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleLabel2.getText();
            
            if (userID.equals("")) {
                JOptionPane.showMessageDialog(frame, "Please enter UserID to modify.");
            } else if (username.equals("")|| password.equals("")) {
                JOptionPane.showMessageDialog(frame, "Username and Password Fields must be filled.");
            } else if (username.contains(" ") || password.contains(" ")) {
                JOptionPane.showMessageDialog(frame,"Username and Password cannot contain spaces.");
            } else if (!username.equals(originalUsername) && UserUtils.userExists(username)) {
                JOptionPane.showMessageDialog(frame,"Username already in use, please enter another one.");
            } else {
                try (BufferedReader br = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
                    String line;
                    java.util.List<String> lines = new java.util.ArrayList<>();

                    while ((line = br.readLine()) != null) {
                        if (line.contains("UserID: " + userID + " | ")) {
                            String updatedDetails = ("UserID: "+userID+" | Username: "+username+" | Password: "+password+" | Role: "+role);
                            lines.add(updatedDetails);
                        } else {
                             lines.add(line);
                        }
                    }

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter("txtfiles/users.txt"))) {
                        for (String updatedLine : lines) {
                            bw.write(updatedLine);
                            bw.newLine();
                        }
                        JOptionPane.showMessageDialog(frame, "User details updated successfully!");
                        resetButton.doClick();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error saving changes to file.");
                    }

                } catch (IOException ex) {
                    System.out.println("test");
                }
            }
        }
    }

    public static void main(String[] args) {
        new accountUpdate();
    }

}