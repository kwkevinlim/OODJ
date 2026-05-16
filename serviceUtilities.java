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

    //function to convert string from services.txt to Service object
    public static service convertToObject(String line) {
        String[] parts = line.split(" \\| ");
        String serviceID = parts[0].split(": ")[1];
        String serviceName = parts[1].split(": ")[1];
        String price = parts[2].split(": ")[1];
        String duration = parts[3].split(": ")[1];
        return new service(serviceID, serviceName, price, duration);
    }

    //helper to load data into table from services.txt
    public static void loadServiceData(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                service s = convertToObject(line);
                model.addRow(new Object[]{s.getServiceID(), s.getServiceName(), s.getPrice(), s.getDuration()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //helper to write updated details to text file
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

    //helper to get details based on serviceid from text file
      public static service getServiceDetails(String serviceID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                service s = convertToObject(line);
                if (s.getServiceID().equals(serviceID)) {
                    return s;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        return null;
    }

    

}
