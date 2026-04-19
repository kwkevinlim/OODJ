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

public abstract class UserUtils {

    private UserUtils() {}

    public static void newSession() {
        try (FileWriter writer = new FileWriter("txtfiles/role.txt", false)) {
        } catch (IOException e) {
            System.out.println("Error clearing file.");
        }
    }

    public static void setSessionRole(String role) {
        try (FileWriter writer = new FileWriter("txtfiles/role.txt", false)) {
            writer.write(role);
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

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
    
    public static boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" | ");
                if (parts.length == 4) {
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

    public static void saveUser(String username, String password, String selectedRole, JFrame frame) {
        List<String> lines = new ArrayList<>();
        boolean filledBlank = false;
        int userID = 1;
        String line;
        
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() && !filledBlank) {
                    lines.add("UserID: " + userID + " | Username: " + username + " | Password: " + password + " / Role: " + selectedRole);
                    filledBlank = true;
                } else {
                    lines.add(line);
                }
                if (!line.trim().isEmpty()) {
                    userID++;
                }
            }
            if (!filledBlank) {
                lines.add("UserID: " + userID + " | Username: " + username + " | Password: " + password + " / Role: " + selectedRole);
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

    public static void loadUserData(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(" | ");
                String userId = parts[0].split(": ")[1];
                String username = parts[1].split(": ")[1];
                String password = parts[2].split(": ")[1];
                String role = parts[3].split(": ")[1];
                model.addRow(new Object[]{userId, username, password, role});
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
}
