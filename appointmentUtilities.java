import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

public class appointmentUtilities {
    //helper to load appointment details into table
    public static void loadAppointmentData(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(" \\| ");
                String appointmentID = parts[0].split(": ")[1].trim();
                String customerName = parts[1].split(": ")[1].trim();
                String service = parts[2].split(": ")[1].trim();
                String technicianName = parts[3].split(": ")[1].trim();
                String carModel = parts[4].split(": ")[1].trim();
                String carPlateNumber = parts[5].split(": ")[1].trim();
                String appointmentDate = parts[6].split(": ")[1].trim();
                String appointmentTime = parts[7].split(": ")[1].trim();
                String appointmentStatus = parts[8].split(": ")[1].trim();
                model.addRow(new Object[] { appointmentID, customerName, service, technicianName, carModel, carPlateNumber, appointmentDate, appointmentTime, appointmentStatus });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }

    //helper to generate appointmentID based on largest id value in appointments.txt
    public static String generateAppointmentID(){
        int count = 0;
        try (BufferedReader reader = new BufferedReader (new FileReader ("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine())!=null) {
                if (!line.trim().isEmpty()) count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format ("APT%03d", count + 1);
    }

    //helper to delete appointments
    public static void deleteAppointment(String appointmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                if (!line.contains("Appointment ID: " + appointmentID + " |")) {
                    content.append(line).append("\n");
                }
            }
            java.nio.file.Files.write(java.nio.file.Paths.get("txtfiles/appointments.txt"), content.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }


    //helper to add new appointments
    public static void addAppointment (String appointmentID, String customerName, String service, String technicianName, String carModel, String carPlateNumber, String appointmentDate, String appointmentTime, String appointmentStatus) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("txtfiles/appointments.txt", true))) {
            writer.write("Appointment ID: " + appointmentID + " | Customer Name: " + customerName + " | Service: " + service + " | Technician Name: " + technicianName + " | Car Model: " + carModel + " | Car Plate Number: " + carPlateNumber + " | Appointment Date: " + appointmentDate + " | Appointment Time: " + appointmentTime + " | Appointment Status: " + appointmentStatus);
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error writing to file.");
        }
    }


    //helper to update appointment details
    public static void updateAppointment(String appointmentID, String customerName, String service, String technicianName, String carModel, String carPlateNumber, String appointmentDate, String appointmentTime, String appointmentStatus) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader (new FileReader ("txtfiles/appointments.txt"))){
            String line;
            while ((line = reader.readLine())!=null){
                if (line.trim().isEmpty()) {
                    lines.add(line);
                    continue;
                }
                if (line.contains("Appointment ID: " + appointmentID + " |")) {
                    lines.add("Appointment ID: " + appointmentID + " | Customer Name: " + customerName + " | Service: " + service + " | Technician Name: " + technicianName + " | Car Model: " + carModel + " | Car Plate Number: " + carPlateNumber + " | Appointment Date: " + appointmentDate + " | Appointment Time: " + appointmentTime + " | Appointment Status: " + appointmentStatus);
                } else {
                    lines.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }

        try (BufferedWriter writer = new BufferedWriter (new FileWriter ("txtfiles/appointments.txt"))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
            System.out.println("Appointment updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error writing to file.");
        }
    }

    //helper to add customer names from users.txt into dropdown selecter
    public static void loadCustomers(java.awt.Choice customerNameChoice) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                if (line.contains("Role: Customer")) {
                    String[] parts = line.split(" \\| ");
                    String customerName = parts[4].split(": ")[1].trim();
                    customerNameChoice.add(customerName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }

    //helper to add service types from services.txt into dropdown selecter
    public static void loadServices(java.awt.Choice serviceChoice) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(" \\| ");
                String serviceName = parts[1].split(": ")[1].trim();
                serviceChoice.add(serviceName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }

    //helper to add technician names from users.txt into dropdown selecter
    public static void loadTechnicians(java.awt.Choice technicianNameChoice) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                if (line.contains("Role: Technician")) {
                    String[] parts = line.split(" \\| ");
                    String technicianName = parts[4].split(": ")[1].trim();
                    technicianNameChoice.add(technicianName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }
}
