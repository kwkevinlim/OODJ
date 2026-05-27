import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class appointmentsPage implements ActionListener, manageable {
    private JFrame frame = new JFrame();
    private JLabel titleLabel = new JLabel("Appointments");
    private JButton returnButton = new JButton("Return to Main Page");
    private JTable appointmentTable;
    private JButton addAppointmentButton = new JButton("Add Appointment");
    private JButton editAppointmentButton = new JButton("Edit Appointment");
    private JButton saveChangesButton = new JButton("Save Changes");
    private JButton cancelButton = new JButton("Cancel");
    private JDialog appointmentDialog = new JDialog(frame, "Appointment Details", true);

    private JLabel appointmentIDLabel = new JLabel("Appointment ID: ");
    private JLabel customerNameLabel = new JLabel("Customer Name: ");
    private java.awt.Choice customerName = new java.awt.Choice();

    private JLabel serviceLabel = new JLabel("Service: ");
    private java.awt.Choice service = new java.awt.Choice();

    private JLabel technicianNameLabel = new JLabel("Technician Name: ");
    private java.awt.Choice technicianName = new java.awt.Choice();

    private JLabel carModelLabel = new JLabel("Car Model: ");
    private JTextField carModelField = new JTextField();

    private JLabel carPlateNumberLabel = new JLabel("Car Plate Number: ");
    private JTextField carPlateNumberField = new JTextField();

    private JLabel appointmentDateLabel = new JLabel("Appointment Date: ");
    private JLabel appointmentTimeLabel = new JLabel("Appointment Time: ");

    private JLabel appointmentStatusLabel = new JLabel("Appointment Status: ");
    private Choice appointmentStatusChoice = new Choice();

    // NEW: Priority field for Counter Staff
    private JLabel priorityLabel = new JLabel("Priority: ");
    private Choice priorityChoice = new Choice();

    private JSpinner appointmentDateSpinner = new JSpinner(new SpinnerDateModel());
    private JSpinner appointmentTimeSpinner = new JSpinner(new SpinnerDateModel());

    private String appointmentID = "";
    private Boolean isNewAppointment = true;
    private JButton deleteAppointmentButton = new JButton("Delete Appointment");

    public appointmentsPage() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Appointment ID",
                "Customer Name",
                "Service",
                "Technician Name",
                "Car Model",
                "Car Plate Number",
                "Appointment Date",
                "Appointment Time",
                "Appointment Status"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(model);
        appointmentUtilities.loadAppointmentData(model);

        appointmentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && appointmentTable.getSelectedRow() != -1) {
                    appointmentID = appointmentTable.getValueAt(appointmentTable.getSelectedRow(), 0).toString();
                }
            }
        });

        frame.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addAppointmentButton);
        buttonPanel.add(editAppointmentButton);
        buttonPanel.add(returnButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addAppointmentButton.addActionListener(this);
        editAppointmentButton.addActionListener(this);
        returnButton.addActionListener(this);
        saveChangesButton.addActionListener(this);
        cancelButton.addActionListener(this);
        deleteAppointmentButton.addActionListener(this);

        returnButton.setBackground(new Color(220, 50, 50));
        returnButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(220, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        deleteAppointmentButton.setBackground(new Color(220, 50, 50));
        deleteAppointmentButton.setForeground(Color.WHITE);
        saveChangesButton.setBackground(new Color(0, 180, 0));
        saveChangesButton.setForeground(Color.WHITE);

        addAppointmentButton.setBackground(Color.LIGHT_GRAY);
        editAppointmentButton.setBackground(Color.LIGHT_GRAY);

        addAppointmentButton.setFocusable(false);
        editAppointmentButton.setFocusable(false);
        returnButton.setFocusable(false);
        saveChangesButton.setFocusable(false);
        cancelButton.setFocusable(false);
        deleteAppointmentButton.setFocusable(false);

        // NEW: Dialog changed from 9 rows to 10 rows because Priority is added
        JPanel dialogFields = new JPanel(new GridLayout(10, 2, 5, 5));

        dialogFields.add(appointmentIDLabel);
        dialogFields.add(new JLabel());

        dialogFields.add(customerNameLabel);
        dialogFields.add(customerName);

        dialogFields.add(serviceLabel);
        dialogFields.add(service);

        dialogFields.add(technicianNameLabel);
        dialogFields.add(technicianName);

        dialogFields.add(carModelLabel);
        dialogFields.add(carModelField);

        dialogFields.add(carPlateNumberLabel);
        dialogFields.add(carPlateNumberField);

        dialogFields.add(appointmentDateLabel);
        dialogFields.add(appointmentDateSpinner);

        dialogFields.add(appointmentTimeLabel);
        dialogFields.add(appointmentTimeSpinner);

        dialogFields.add(appointmentStatusLabel);
        dialogFields.add(appointmentStatusChoice);

        // NEW: Priority dropdown added into appointment dialog
        dialogFields.add(priorityLabel);
        dialogFields.add(priorityChoice);

        JPanel dialogButtons = new JPanel(new FlowLayout());
        dialogButtons.add(saveChangesButton);
        dialogButtons.add(cancelButton);
        dialogButtons.add(deleteAppointmentButton);

        appointmentDialog.setLayout(new BorderLayout());
        appointmentDialog.add(dialogFields, BorderLayout.CENTER);
        appointmentDialog.add(dialogButtons, BorderLayout.SOUTH);
        appointmentDialog.setSize(450, 430);
        appointmentDialog.setLocationRelativeTo(frame);

        appointmentUtilities.loadCustomers(customerName);
        appointmentUtilities.loadServices(service);
        appointmentUtilities.loadTechnicians(technicianName);

        appointmentStatusChoice.add("Pending");
        appointmentStatusChoice.add("In Progress");
        appointmentStatusChoice.add("Completed");
        appointmentStatusChoice.add("Cancelled");

        // NEW: Priority options
        priorityChoice.add("Normal");
        priorityChoice.add("High");

        appointmentDateSpinner.setEditor(new JSpinner.DateEditor(appointmentDateSpinner, "yyyy-MM-dd"));
        appointmentTimeSpinner.setEditor(new JSpinner.DateEditor(appointmentTimeSpinner, "HH:mm"));

        frame.setVisible(true);
    }

    public String displayAppointmentDetails(String appointmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (line.contains("Appointment ID: " + appointmentID + " |")) {
                    String[] parts = line.split(" \\| ");

                    customerName.select(parts[1].split(": ")[1].trim());
                    service.select(parts[2].split(": ")[1].trim());
                    technicianName.select(parts[3].split(": ")[1].trim());
                    carModelField.setText(parts[4].split(": ")[1].trim());
                    carPlateNumberField.setText(parts[5].split(": ")[1].trim());

                    appointmentDateSpinner.setValue(
                            new java.text.SimpleDateFormat("yyyy-MM-dd").parse(parts[6].split(": ")[1].trim())
                    );

                    appointmentTimeSpinner.setValue(
                            new java.text.SimpleDateFormat("HH:mm").parse(parts[7].split(": ")[1].trim())
                    );

                    appointmentStatusChoice.select(parts[8].split(": ")[1].trim());

                    // NEW: Load priority if it exists, otherwise default to Normal
                    if (line.contains("Priority:")) {
                        String priority = getValue(line, "Priority:");
                        priorityChoice.select(priority);
                    } else {
                        priorityChoice.select("Normal");
                    }

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
        String role = userUtilities.getUserRole();

        String chosenCustomerName = customerName.getSelectedItem();
        String chosenService = service.getSelectedItem();
        String chosenTechnicianName = technicianName.getSelectedItem();
        String carModel = carModelField.getText();
        String carPlateNumber = carPlateNumberField.getText();

        String appointmentDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(appointmentDateSpinner.getValue());
        String appointmentTime = new java.text.SimpleDateFormat("HH:mm").format(appointmentTimeSpinner.getValue());

        String appointmentStatus = "Pending";

        // NEW: Get selected priority
        String selectedPriority = priorityChoice.getSelectedItem();

        if (e.getSource() == returnButton) {
            frame.dispose();

            if (role.equals("Counter Staff")) {
                new csPage();
            } else if (role.equals("Manager")) {
                new managerPage();
            }

        } else if (e.getSource() == addAppointmentButton) {
            isNewAppointment = true;
            appointmentID = appointmentUtilities.generateAppointmentID();
            appointmentIDLabel.setText("Appointment ID: " + appointmentID);

            customerName.setEnabled(true);
            technicianName.setEnabled(true);
            carModelField.setEnabled(true);
            carPlateNumberField.setEnabled(true);
            service.setEnabled(true);

            appointmentStatusChoice.select("Pending");
            appointmentStatusChoice.setEnabled(false);

            // NEW: Counter staff can choose priority when creating appointment
            priorityChoice.select("Normal");
            priorityChoice.setEnabled(true);

            deleteAppointmentButton.setVisible(false);
            appointmentDialog.setVisible(true);

        } else if (e.getSource() == editAppointmentButton) {
            if (appointmentID.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select an appointment first.");
                return;
            }

            isNewAppointment = false;
            displayAppointmentDetails(appointmentID);
            appointmentIDLabel.setText("Appointment ID: " + appointmentID);

            customerName.setEnabled(false);
            technicianName.setEnabled(false);
            carModelField.setEnabled(false);
            carPlateNumberField.setEnabled(false);
            service.setEnabled(false);

            appointmentStatusChoice.setEnabled(true);

            // NEW: Counter staff can also edit priority later
            priorityChoice.setEnabled(true);

            deleteAppointmentButton.setVisible(true);
            appointmentDialog.setVisible(true);

        } else if (e.getSource() == saveChangesButton) {
            appointmentStatus = appointmentStatusChoice.getSelectedItem();
            selectedPriority = priorityChoice.getSelectedItem();

            if (carModel.isEmpty() || carPlateNumber.isEmpty()) {
                JOptionPane.showMessageDialog(appointmentDialog, "Car Model and Car Plate Number cannot be empty.");
                return;
            }

            if (isNewAppointment) {
                appointmentUtilities.addAppointment(
                        appointmentID,
                        chosenCustomerName,
                        chosenService,
                        chosenTechnicianName,
                        carModel,
                        carPlateNumber,
                        appointmentDate,
                        appointmentTime,
                        appointmentStatus
                );

                // NEW: Add priority after appointment is created
                updateAppointmentPriority(appointmentID, selectedPriority);

            } else {
                appointmentUtilities.updateAppointment(
                        appointmentID,
                        chosenCustomerName,
                        chosenService,
                        chosenTechnicianName,
                        carModel,
                        carPlateNumber,
                        appointmentDate,
                        appointmentTime,
                        appointmentStatus
                );

                // NEW: Preserve/update priority after appointment is edited
                updateAppointmentPriority(appointmentID, selectedPriority);
            }

            appointmentDialog.setVisible(false);

            DefaultTableModel model = (DefaultTableModel) appointmentTable.getModel();
            model.setRowCount(0);
            appointmentUtilities.loadAppointmentData(model);

        } else if (e.getSource() == cancelButton) {
            appointmentDialog.setVisible(false);

        } else if (e.getSource() == deleteAppointmentButton) {
            if (appointmentID.isEmpty()) {
                return;
            }

            appointmentUtilities.deleteAppointment(appointmentID);
            appointmentDialog.setVisible(false);

            DefaultTableModel model = (DefaultTableModel) appointmentTable.getModel();
            model.setRowCount(0);
            appointmentUtilities.loadAppointmentData(model);
        }
    }

    // NEW: Helper method to read values like Priority from appointment line
    private String getValue(String line, String key) {
        String[] parts = line.split("\\|");

        for (String part : parts) {
            part = part.trim();

            if (part.startsWith(key)) {
                return part.substring(key.length()).trim();
            }
        }

        return "";
    }

    // NEW: This method updates or adds Priority inside appointments.txt
    private void updateAppointmentPriority(String appointmentID, String selectedPriority) {
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Appointment ID: " + appointmentID + " |")) {
                    if (line.contains("Priority:")) {
                        line = line.replaceAll("Priority: .*", "Priority: " + selectedPriority);
                    } else {
                        line = line + " | Priority: " + selectedPriority;
                    }
                }

                updatedLines.add(line);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error reading appointments file.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/appointments.txt"))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating appointment priority.");
        }
    }

    public static void main(String[] args) {
        new appointmentsPage();
    }

    @Override
    public void loadData(DefaultTableModel model) {
        appointmentUtilities.loadAppointmentData(model);
    }

    @Override
    public void saveData() {
    }
}
