import javax.swing.*;
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
    private JScrollPane scrollPane;
    private JButton addAppointmentButton = new JButton("Add Appointment");
    private JButton editAppointmentButton = new JButton("Edit Appointment");
    private JButton saveChangesButton = new JButton("Save Changes");
    private JButton cancelButton = new JButton("Cancel");
    private JDialog appointmentDialog = new JDialog(frame, "Appointment Details", true);
    private JLabel appointmentIDLabel = new JLabel("Appointment ID: ");
    private JLabel customerNameLabel = new JLabel("Customer Name: ");
    private java.awt.Choice customerNameChoice = new java.awt.Choice();
    private JLabel serviceLabel = new JLabel("Service: ");
    private java.awt.Choice serviceChoice = new java.awt.Choice();
    private JLabel technicianNameLabel = new JLabel("Technician Name: "); 
    private java.awt.Choice technicianNameChoice = new java.awt.Choice();
    private JLabel carModelLabel = new JLabel("Car Model: ");
    private JTextField carModelField = new JTextField();
    private JLabel carPlateNumberLabel = new JLabel("Car Plate Number: ");
    private JTextField carPlateNumberField = new JTextField();
    private JLabel appointmentDateLabel = new JLabel("Appointment Date: ");
    private JLabel technicianPhoneNumberLabel = new JLabel("Technician Phone Number: "); //extracted from users.txt
    private JLabel customerPhoneNumberLabel = new JLabel("Customer Phone Number: "); //extracted from users.txt
    private JLabel appointmentTimeLabel = new JLabel("Appointment Time: ");
    private JLabel appointmentStatusLabel = new JLabel("Appointment Status: ");
    private java.awt.Choice appointmentStatusChoice = new java.awt.Choice();

    //What's left:
    //1. Appointment ratings have to be matched to feedback.java, but that's for later
    //2. date and time picker

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
