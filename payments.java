import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class payments implements ActionListener {

    // gui components
    private JFrame frame = new JFrame();
    private JLabel titleLabel = new JLabel("Manage Appointment Payments");
    private JButton returnButton = new JButton("Return to Main Page");
    private JTable paymentStatusTable;
    private JDialog paymentCollectionDialog = new JDialog(frame, "Payment Collection", true);
    private JLabel searchLabel = new JLabel("Search for an appointment:");
    private JTextField searchBar = new JTextField();
    private JButton collectPaymentButton = new JButton("Collect Payment");
    private JButton cancelButton = new JButton("Cancel");
    private JButton searchButton = new JButton("Search");
    private Choice paymentStatusFilter = new Choice();
    private JButton generateReceiptButton = new JButton("Generate Receipts");
    private JPanel buttonPanel = new JPanel(new FlowLayout());
    String appointmentID;
    String customerName;
    String paymentDate;
    String paymentTime;
    Boolean recordFound = false;
    private JButton confirmButton = new JButton("Confirm Payment");

    public payments() {

        // gui layout
        frame.setTitle("Manage Appointment Payments");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLocationRelativeTo(null);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        String[] columns = { "Appointment ID", "Customer Name", "Service", "Technician Name", "Car Model",
                "Car Plate Number", "Appointment Date", "Appointment Time", "Appointment Status", "Payment Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        paymentStatusTable = new JTable(model);
        tableCombiner(model);

        frame.add(new JScrollPane(paymentStatusTable), BorderLayout.CENTER);

        buttonPanel.add(collectPaymentButton);
        buttonPanel.add(generateReceiptButton);
        buttonPanel.add(returnButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        returnButton.setBackground(new Color(220, 50, 50));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusable(false);
        collectPaymentButton.setBackground(Color.LIGHT_GRAY);
        collectPaymentButton.setFocusable(false);
        generateReceiptButton.setBackground(Color.LIGHT_GRAY);
        generateReceiptButton.setFocusable(false);
        searchButton.setBackground(Color.LIGHT_GRAY);
        searchButton.setFocusable(false);
        confirmButton.setBackground(new Color(0, 180, 0));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusable(false);
        cancelButton.setBackground(new Color(220, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusable(false);

        paymentCollectionDialog.setLayout(new FlowLayout());
        paymentCollectionDialog.setSize(400, 200);
        paymentCollectionDialog.setLocationRelativeTo(frame);

        searchLabel.setPreferredSize(new Dimension(200, 25));
        searchBar.setPreferredSize(new Dimension(200, 25));
        searchButton.setPreferredSize(new Dimension(80, 25));
        confirmButton.setPreferredSize(new Dimension(130, 25));
        cancelButton.setPreferredSize(new Dimension(80, 25));

        paymentCollectionDialog.add(searchLabel);
        paymentCollectionDialog.add(searchBar);
        paymentCollectionDialog.add(searchButton);
        paymentCollectionDialog.add(confirmButton);
        paymentCollectionDialog.add(cancelButton);
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);

        paymentStatusFilter.add("Paid");
        paymentStatusFilter.add("Unpaid");

        returnButton.addActionListener(this);
        collectPaymentButton.addActionListener(this);
        cancelButton.addActionListener(this);
        searchButton.addActionListener(this);
        generateReceiptButton.addActionListener(this);
        confirmButton.addActionListener(this);

        frame.setVisible(true);
    }

    public static void tableCombiner(DefaultTableModel model) {
        ArrayList<String> paidIDs = new ArrayList();
        // reads paid appointment ids from payments.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/payments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split("\\|");
                // takes appointment IDs from payments.txt
                paidIDs.add(parts[1].split(": ")[1].trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // reads appointment ids and cross checks
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
                String paymentStatus = paidIDs.contains(appointmentID) ? "Paid" : "Unpaid";
                model.addRow(new Object[] { appointmentID, customerName, service, technicianName, carModel,
                        carPlateNumber, appointmentDate, appointmentTime, appointmentStatus, paymentStatus });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // button actions
    @Override
    public void actionPerformed(ActionEvent e) {
        String csID = userUtilities.getUserID();
        if (e.getSource() == returnButton) {
            frame.dispose();
            new csPage();
        } else if (e.getSource() == collectPaymentButton) {
            paymentCollectionDialog.setVisible(true);
        } else if (e.getSource() == cancelButton) {
            paymentCollectionDialog.setVisible(false);
        } else if (e.getSource() == searchButton) {
            appointmentID = searchBar.getText();
            String[] appointmentDetails = paymentUtilities.appointmentIDExtractor(appointmentID);
            if (appointmentDetails != null) {
                appointmentID = appointmentDetails[0];
                customerName = appointmentDetails[1];
                recordFound = paymentUtilities.appointmentSearch(appointmentID);
                boolean isPaid = paymentUtilities.isPaid(appointmentID);
                if (isPaid) {
                    JOptionPane.showMessageDialog(null, "Error, this appointment has already been paid.");
                } else {
                    confirmButton.setVisible(true);
                    cancelButton.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error, appointment record not found or appointment is still in progress.");
            }
        } else if (e.getSource() == generateReceiptButton) {
            int selectedRow = paymentStatusTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Error, no payment record selected.");
            } else {
                appointmentID = paymentStatusTable.getValueAt(selectedRow, 0).toString();
                paymentUtilities.generateReceipt(appointmentID, csID, paymentDate, paymentTime);
                JOptionPane.showMessageDialog(null, "Receipt has been successfully generated.");
            }
        } else if (e.getSource() == confirmButton) {
            String paymentID = paymentUtilities.generatePaymentID();
            String customerID = paymentUtilities.customerIDExtractor(customerName);
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            paymentDate = currentDate.format(dateFormatter);
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            paymentTime = currentTime.format(timeFormatter);
            paymentUtilities.savePayment(paymentID, appointmentID, customerID, csID, paymentDate, paymentTime);
            paymentUtilities.generateReceipt(appointmentID, csID, paymentDate, paymentTime);
            JOptionPane.showMessageDialog(null, "Payment has been successfully collected.");
            paymentCollectionDialog.setVisible(false);
            confirmButton.setVisible(false);
            cancelButton.setVisible(false);
            searchBar.setText("");
            recordFound = false;
            DefaultTableModel model = (DefaultTableModel) paymentStatusTable.getModel();
            model.setRowCount(0);
            tableCombiner(model);
        }
    }

    public static void main(String[] args) {
        new payments();
    }

}
