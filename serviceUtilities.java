import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class serviceUtilities {
    public static void loadServiceData(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] data = line.split(" \\| ");
                String serviceID = data[0].split(": ")[1];
                String serviceName = data[1].split(": ")[1];
                String price = data[2].split(": ")[1];
                String duration = data[3].split(": ")[1];
                data = new String[] { serviceID, serviceName, price, duration };
                model.addRow(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateServiceDetails(String serviceID, String serviceName, String price, String duration) {
                List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    lines.add(line);
                    continue;
                }
                String[] parts = line.split(" \\| ");
                String storedServiceId = parts[0].split(": ")[1];
                if (storedServiceId.equals(serviceID)) {
                    lines.add("Service ID: " + serviceID + " | Service Name: " + serviceName + " | Price: " + price + 
                    " | Duration: " + duration);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/services.txt"))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
            JOptionPane.showMessageDialog(null, "Service details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

      public static String getServiceDetails(String serviceID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (line.contains("Service ID: " + serviceID + " |")) {
                    return line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return null;
    }

    

}
