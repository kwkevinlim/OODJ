import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

public class paymentUtilities {
    // helper to add new payment records
    public static void savePayment(String paymentID, String appointmentID, String customerID, String csID,
            String paymentDate, String paymentTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/payments.txt", true))) {
            writer.write("Payment ID: " + paymentID + " | Appointment ID: " + appointmentID + " | customerID: "
                    + customerID + " | csID: " + csID + " | Payment Date: " + paymentDate + " | Payment Time: "
                    + paymentTime);
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading from file.");
        }
    }

    // helper to generate payment IDs
    public static String generatePaymentID() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/payments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty())
                    count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("PAY%03d", count + 1);
    }

    // helper to search for appointment records
    public static boolean appointmentSearch(String appointmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(" \\| ");
                if (parts.length == 9 && line.contains("Appointment ID: " + appointmentID)) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot identify appointment.");
            return false;
        }
    }

    // helper to extract appointmentID and customerName
    public static String[] appointmentIDExtractor(String appointmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(" \\| ");
                if (parts.length == 9) {
                    String storedAppointmentID = parts[0].split(": ")[1].trim();
                    String customerName = parts[1].split(": ")[1].trim();
                    String appointmentSatus = parts[8].split(": ")[1].trim();
                    if (storedAppointmentID.equals(appointmentID) && appointmentSatus.equals("Completed")) {
                        return new String[] { storedAppointmentID, customerName };
                    }
                }
            }
            System.out.println("Record not found.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot locate appointment record.");
            return null;
        }
    }

    //helper to check if appointment has already been paid for
    public static boolean isPaid(String appointmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/payments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (line.contains("Appointment ID: " + appointmentID)) return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // helper to extract user id from customer name listed on appointment
    public static String customerIDExtractor(String customerName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(" \\| ");
                if (parts.length == 10 && line.contains("Legal Name: " + customerName)) {
                    return parts[0].split(": ")[1].trim();
                }
            }
            System.out.println("Record not found.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot locate users file");
            return null;
        }
    }

    // helper to generate receipts
    public static void generateReceipt (String appointmentID, String csID, String paymentDate, String paymentTime) {
        String serviceName = null;
        String receiptID = null;
        String customerName = null;
        String technicianName = null;
        String carModel = null;
        String carPlateNumber = null;
        String appointmentDate = null;
        String appointmentTime = null;
        String csName= null;
        String price = null;
        int maxID = 0;
        File folder = new File("txtfiles/receipts/");
        File[] receipts = folder.listFiles();
        if (receipts != null) {
            for (File receipt : receipts) {
                String Filename = receipt.getName();
                if (Filename.startsWith("REC") && Filename.endsWith(".txt")) {
                    try {
                        int num = Integer.parseInt(Filename.substring(3, 6));
                        if (num > maxID) {
                            maxID = num;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    
        receiptID = String.format("REC%03d", maxID + 1);

    try(BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))){
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            String[] parts = line.split(" \\| ");
            if (parts.length == 9 && line.contains("Appointment ID: " + appointmentID)) {
                customerName = parts[1].split(": ")[1].trim();
                serviceName = parts[2].split(": ")[1].trim();
                technicianName = parts[3].split(": ")[1].trim();
                carModel = parts[4].split(": ")[1].trim();
                carPlateNumber = parts[5].split(": ")[1].trim();
                appointmentDate = parts[6].split(": ")[1].trim();
                appointmentTime = parts[7].split(": ")[1].trim();
            }
        }
    }catch(Exception e){
        e.printStackTrace();
        System.out.println("Error reading from file.");
    }

    try(BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))){
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            String[] parts = line.split(" \\| ");
            if (line.contains("Service Name: " + serviceName)) {
                price = parts[2].split(": ")[1].trim();
            }
        }
    }catch(
    Exception e)
    {
        e.printStackTrace();
        System.out.println("Error reading from file.");
    }

    try(BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))){
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty())
                continue;
            String[] parts = line.split(" \\| ");
            if (parts.length == 10 && line.contains("UserID: " + csID)) {
                csName = parts[4].split(": ")[1].trim();
            }
        }
    }catch(Exception e)
    {
        e.printStackTrace();
        System.out.println("Error reading from file.");
    }

    try(BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/receipts/" + receiptID + ".txt", true))){
        writer.write("=========================================\n" + "  APU Automotive Service Center Sdn Bhd\n"
                + "             Service Receipt\n" + "=========================================\n"
                + "Receipt ID: " + receiptID + "\nReceipt Date: " + paymentDate + ", " + paymentTime 
                + "\n-----------------------------------------" + "\nCustomer Name: " + customerName 
                + "\nCar Model: " + carModel + "\nCar Plate Number: " + carPlateNumber + "\n-----------------------------------------" 
                +"\nService Type: " + serviceName + "\nDrop Off Date: " + appointmentDate + ", " + appointmentTime
                + "\nTechnician: " + technicianName + "\n-----------------------------------------" 
                + "\nTotal Paid: RM " + price + "\nCashier: " + csName + "\n========================================="
                + "\nThank You For Using Our Services. Please Visit Again!");
    }catch(Exception e)
    {
        e.printStackTrace();
        System.out.println("Error generating receipt.");
    }
}

public static void main(String[] args) {
    generateReceipt(null, null, null, null);
}}
