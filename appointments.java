import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class appointments implements ActionListener {
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
    //decided to use JSpinner instead of LGoodCalendar
    private JSpinner appointmentDateSpinner = new JSpinner(new SpinnerDateModel());
    private JSpinner appointmentTimeSpinner = new JSpinner(new SpinnerDateModel());
    private String appointmentID = "";
    private Boolean isNewAppointment = true;
    private JButton deleteAppointmentButton = new JButton("Delete Appointment");

    public appointments() {
        //gui components (there are a lot in this page :o )
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        //sets the table values
        String[] columns = { "Appointment ID", "Customer Name", "Service", "Technician Name", "Car Model",
                "Car Plate Number", "Appointment Date", "Appointment Time", "Appointment Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(model);
        appointmentUtilities.loadAppointmentData(model);


        //function to get appointment ID based on the table row I choose
        appointmentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && appointmentTable.getSelectedRow() !=1)
                    appointmentID = appointmentTable.getValueAt(appointmentTable.getSelectedRow(), 0).toString();
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

        addAppointmentButton.setBackground(Color.LIGHT_GRAY);
        editAppointmentButton.setBackground(Color.LIGHT_GRAY);
        returnButton.setBackground(Color.LIGHT_GRAY);
        saveChangesButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setBackground(Color.LIGHT_GRAY);
        deleteAppointmentButton.setBackground(Color.LIGHT_GRAY);
        addAppointmentButton.setFocusable(false);
        editAppointmentButton.setFocusable(false);
        returnButton.setFocusable(false);
        saveChangesButton.setFocusable(false);
        cancelButton.setFocusable(false);
        deleteAppointmentButton.setFocusable(false);

        appointmentDialog.add(customerNameLabel);
        appointmentDialog.add(customerName);
        appointmentDialog.add(serviceLabel);
        appointmentDialog.add(service);
        appointmentDialog.add(technicianNameLabel);
        appointmentDialog.add(technicianName);
        appointmentDialog.add(carModelLabel);
        appointmentDialog.add(carModelField);
        appointmentDialog.add(carPlateNumberLabel);
        appointmentDialog.add(carPlateNumberField);
        appointmentDialog.add(appointmentDateLabel);
        appointmentDialog.add(appointmentDateSpinner);
        appointmentDialog.add(appointmentTimeLabel);
        appointmentDialog.add(appointmentTimeSpinner);
        appointmentDialog.add(appointmentStatusLabel);
        appointmentDialog.add(appointmentStatusChoice);
        appointmentDialog.add(saveChangesButton);
        appointmentDialog.add(cancelButton);
        appointmentDialog.add(appointmentIDLabel);
        appointmentDialog.setSize(400, 400);
        appointmentDialog.add(deleteAppointmentButton);

        // Load customer names, services types, and technicna names into choices from their textfiles
        appointmentUtilities.loadCustomers(customerName);
        appointmentUtilities.loadServices(service);
        appointmentUtilities.loadTechnicians(technicianName);

        //options for status, but default is pending
        appointmentStatusChoice.add("Pending");
        appointmentStatusChoice.add("In Progress");
        appointmentStatusChoice.add("Completed");
        //added cancelled because why not? Users or technicians can cancel
        appointmentStatusChoice.add("Cancelled");

        //formats for date and time
        appointmentDateSpinner.setEditor(new JSpinner.DateEditor(appointmentDateSpinner, "yyyy-MM-dd"));
        appointmentTimeSpinner.setEditor(new JSpinner.DateEditor(appointmentTimeSpinner, "HH:mm"));

        frame.setVisible(true);
    }

    //method to display appointment details in editing dialog based on id which was obtained earlier
    public String displayAppointmentDetails(String appointmentID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                if (line.contains("Appointment ID: " + appointmentID + " |")) {
                    String[] parts = line.split(" \\| ");
                    customerName.select(parts[1].split(": ")[1].trim());
                    service.select(parts[2].split(": ")[1].trim());
                    technicianName.select(parts[3].split(": ")[1].trim());
                    carModelField.setText(parts[4].split(": ")[1].trim());
                    carPlateNumberField.setText(parts[5].split(": ")[1].trim());
                    appointmentDateSpinner.setValue(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(parts[6].split(": ")[1].trim()));
                    appointmentTimeSpinner.setValue(new java.text.SimpleDateFormat("HH:mm").parse(parts[7].split(": ")[1].trim()));
                    appointmentStatusChoice.select(parts[8].split(": ")[1].trim());
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
        //variables to get new or edited details from fields
        String role = userUtilities.getUserRole();
        String chosenCustomerName = customerName.getSelectedItem();
        String chosenService = service.getSelectedItem();
        String chosenTechnicianName = technicianName.getSelectedItem();
        String carModel = carModelField.getText();
        String carPlateNumber = carPlateNumberField.getText();
        String appointmentDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(appointmentDateSpinner.getValue());
        String appointmentTime = new java.text.SimpleDateFormat("HH:mm").format(appointmentTimeSpinner.getValue());
        String appointmentStatus = "Pending";
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
            //all fields enabled when it is a new appointment
            customerName.setEnabled(true);
            technicianName.setEnabled(true);
            carModelField.setEnabled(true);
            carPlateNumberField.setEnabled(true);
            service.setEnabled(true);
            appointmentStatusChoice.select("Pending");
            appointmentStatusChoice.setEnabled(false);
            deleteAppointmentButton.setVisible(false);
            appointmentDialog.setVisible(true);
        } else if (e.getSource() == editAppointmentButton) {
            isNewAppointment = false;
            displayAppointmentDetails(appointmentID);
            //only status field enabled for updating status
            customerName.setEnabled(false);
            technicianName.setEnabled(false);
            carModelField.setEnabled(false);
            carPlateNumberField.setEnabled(false);
            service.setEnabled(false);
            appointmentStatusChoice.setEnabled(true);
            deleteAppointmentButton.setVisible(true);
            appointmentDialog.setVisible(true);
        } else if (e.getSource() == saveChangesButton) {
            appointmentStatus = appointmentStatusChoice.getSelectedItem();
            //check to see if details are ok
            if (carModel.isEmpty() || carPlateNumber.isEmpty()) {
                JOptionPane.showMessageDialog(appointmentDialog, "Car Model and Car Plate Number cannot be empty.");
                return;
            }
            //if new appointment use addAppointment helper
            if (isNewAppointment) {
                appointmentUtilities.addAppointment(appointmentID, chosenCustomerName, chosenService, chosenTechnicianName, carModel,
                        carPlateNumber, appointmentDate, appointmentTime, appointmentStatus);
            //if editing existing one use updateAppointment
            } else {
                appointmentUtilities.updateAppointment(appointmentID, chosenCustomerName, chosenService, chosenTechnicianName, carModel,
                        carPlateNumber, appointmentDate, appointmentTime, appointmentStatus);
            }
            appointmentDialog.setVisible(false);
            //refresh table after updating
            DefaultTableModel model = (DefaultTableModel) appointmentTable.getModel();
            model.setRowCount(0);
            appointmentUtilities.loadAppointmentData(model);
        } else if (e.getSource() == cancelButton) {
            appointmentDialog.setVisible(false);
        } else if (e.getSource() == deleteAppointmentButton) {
            if (appointmentID.isEmpty()) return;
            appointmentUtilities.deleteAppointment(appointmentID);
            appointmentDialog.setVisible(false);
            DefaultTableModel model = (DefaultTableModel) appointmentTable.getModel();
            model.setRowCount(0);
            appointmentUtilities.loadAppointmentData(model);
        }
    }

    public static void main(String[] args) {
        new appointments();
    }
}
