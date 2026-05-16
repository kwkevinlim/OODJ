import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public abstract class userUtilities {

    //helper to convert user string rows in users.txt to user objects
    public static user convertToObject(String line) {
        String[] parts = line.split(" \\| ");
        String userID = parts[0].split(": ")[1].trim();
        String username = parts[1].split(": ")[1].trim();
        String password = parts[2].split(": ")[1].trim();
        String role = parts[3].split(": ")[1].trim();
        String legalname = parts.length > 4 ? parts[4].split(": ")[1].trim() : "";
        String email = parts.length > 5 ? parts[5].split(": ")[1].trim() : "";
        String phoneNumber = parts.length > 6 ? parts[6].split(": ")[1].trim() : "";
        String address = parts.length > 7 ? parts[7].split(": ")[1].trim() : "";
        String gender = parts.length > 8 ? parts[8].split(": ")[1].trim() : "";
        String dob = parts.length > 9 ? parts[9].split(": ")[1].trim() : "";
        return new user(userID, username, password, role, legalname, email, 
            phoneNumber, address, gender, dob);
    }

    private userUtilities() {}

    //helper to delete role from role.txt and id from id.txt when user logs out
    public static void newSession() {
        try (FileWriter writer = new FileWriter("txtfiles/role.txt", false)) {
        } catch (IOException e) {
            System.out.println("Error clearing file.");
        }
        try (FileWriter writer = new FileWriter("txtfiles/ID.txt", false)) {
        } catch (IOException e) {
            System.out.println("Error clearing file.");
        }
    }

    //helper to set user role to role.txt, helps with access control
    public static void setUserRole(String role) {
        try (FileWriter writer = new FileWriter("txtfiles/role.txt", false)) {
            writer.write(role);
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    //helper to get user role, access control purposes
    public static String getUserRole() {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/role.txt"))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error reading file.");
            return null;
        }
    }

    //helper to set user id based on current logged in user, also helps with access control, especially edit profile
        public static void setUserID(String ID) {
        try (FileWriter writer = new FileWriter("txtfiles/ID.txt", false)) {
            writer.write(ID);
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    //helper to check if the users.txt file exist
    public static void checkFilePath() {
        File file = new File("txtfiles/users.txt");
        try {
            file.getParentFile().mkdirs();
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("File creation failed");
            return;
        }
    }
    
    //helper to check if username has been taken
    public static boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 10) {
                    String storedUsername = parts[1].split(": ")[1].trim();
                    if (storedUsername.equals(username)) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "File error.");
        }
        return false;
    }

    //helper to save user to users.txt
    public static void saveUser(String username, String password, String selectedRole, JFrame frame,
        String legalname, String email, String phoneNumber, String address, String gender, String dob
    ) {
        List<String> lines = new ArrayList<>();
        boolean filledBlank = false;
        int userID = 1;
        String line;
        
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() && !filledBlank) {
                    lines.add("UserID: " + userID + " | Username: " + username + " | Password: " + password + 
                    " | Role: " + selectedRole + " | Legal Name: " + legalname + " | Email: " + email + 
                    " | Phone Number: " + phoneNumber + " | Address: " + address + 
                    " | Gender: " + gender + " | Date of Birth: " + dob);
                    filledBlank = true;
                } else {
                    lines.add(line);
                }
                if (!line.trim().isEmpty()) {
                    userID++;
                }
            }
            if (!filledBlank) {
                lines.add("UserID: " + userID + " | Username: " + username + " | Password: " + password + 
                " | Role: " + selectedRole + " | Legal Name: " + legalname + " | Email: " + email + 
                " | Phone Number: " + phoneNumber + " | Address: " + address + 
                " | Gender: " + gender + " | Date of Birth: " + dob);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/users.txt"))) {
            for (String currentline : lines) {
                writer.write(currentline);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "UserID: " + userID + "\nUsername: " + username + "\nPassword: " + password + "\nRole: " + selectedRole, "Account successfully created!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error writing file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //loads staff data for manager to access
    public static void loadStaffData(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                user u = convertToObject(line);
                if (u.getRole().equals("Customer")) continue;
                model.addRow(new Object[]{u.getUserID(), u.getUsername(), u.getPassword(), u.getRole()});
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    //loads customer data for counter staff to access
    public static void loadCustomerData(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                user u = convertToObject(line);
                if (!u.getRole().equals("Customer")) continue;
                model.addRow(new Object[]{u.getUserID(), u.getUsername(), u.getPassword(), u.getRole()
                    , u.getLegalName(), u.getEmail(), u.getPhoneNumber(), u.getAddress(), u.getGender(), 
                    u.getDob()});
                }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    //helper to get user id based on current logged in user. Again, for access control
    public static String getUserID() {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/ID.txt"))) {
            String userID = reader.readLine();
            return userID;
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return null;
    }

    //helper to get user details based on logged in id, for edit profile
    public static user getUserDetails(String userID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                user u = convertToObject(line);
                if (!u.getUserID().equals(userID)){
                    return u;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return null;
    }

    //checks if profile is complete, used for edit profile and booking appointments
    public static boolean isProfileComplete (){
        user u = getUserDetails(getUserID());
        if (u == null) {
            return false;
        }
        return ! (
            u.getUsername().isEmpty()&&
            u.getPassword().isEmpty()&&
            u.getLegalName().isEmpty()&&
            u.getEmail().isEmpty()&&    
            u.getPhoneNumber().isEmpty()&&
            u.getAddress().isEmpty()&&
            u.getGender().isEmpty()&&
            u.getDob().isEmpty()
        );
    }

    //helper to update user details in the text file
    public static void updateUserDetails(String userID, String username, String password, String role, String legalName, String email, String phoneNumber, String address, String gender, String dob) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    lines.add(line);
                    continue;
                }
                String[] parts = line.split(" \\| ");
                String storedUserID = parts[0].split(": ")[1];
                if (storedUserID.equals(userID)) {
                    lines.add("UserID: " + userID + " | Username: " + username + " | Password: " + password + 
                    " | Role: " + role + " | Legal Name: " + legalName + " | Email: " + email + 
                    " | Phone Number: " + phoneNumber + " | Address: " + address +
                    " | Gender: " + gender + " | Date of Birth: " + dob);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/users.txt"))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
            JOptionPane.showMessageDialog(null, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }
}
