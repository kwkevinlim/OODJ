import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class technicianPage extends JFrame {

    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    private JComboBox<String> statusFilterBox;
    private JTextField searchPlateField;

    private final String technicianName = "Rajesh Kumar";
    private final String appointmentFile = "txtfiles/appointments.txt";
    private final String feedbackFile = "txtfiles/feedback.txt";

    private ArrayList<String> appointmentLines = new ArrayList<>();

    public technicianPage() {
        setTitle("Technician Dashboard");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Technician Dashboard - " + technicianName, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel();

        filterPanel.add(new JLabel("Filter Status:"));
        statusFilterBox = new JComboBox<>(new String[]{
                "All", "Pending", "In Progress", "Completed"
        });
        filterPanel.add(statusFilterBox);

        filterPanel.add(new JLabel("Search Licence Plate:"));
        searchPlateField = new JTextField(15);
        filterPanel.add(searchPlateField);

        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        filterPanel.add(searchButton);
        filterPanel.add(clearButton);

        add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);

        String[] columns = {"Customer Name", "Licence Plate", "Status", "Action"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setRowHeight(38);
        appointmentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        appointmentTable.setDefaultRenderer(Object.class, new PriorityRowRenderer());

        appointmentTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        appointmentTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        JButton editProfileButton = new JButton("Edit Profile");
        JButton logoutButton = new JButton("Logout");

        bottomPanel.add(refreshButton);
        bottomPanel.add(editProfileButton);
        bottomPanel.add(logoutButton);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadAssignedAppointments());
        searchButton.addActionListener(e -> loadAssignedAppointments());

        clearButton.addActionListener(e -> {
            statusFilterBox.setSelectedItem("All");
            searchPlateField.setText("");
            loadAssignedAppointments();
        });

        statusFilterBox.addActionListener(e -> loadAssignedAppointments());

        editProfileButton.addActionListener(e -> {
            new editProfile();
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new Main();
        });

        loadAssignedAppointments();
        setVisible(true);
    }

    private void loadAssignedAppointments() {
        tableModel.setRowCount(0);
        appointmentLines.clear();

        String selectedStatusFilter = statusFilterBox.getSelectedItem().toString();
        String plateSearch = searchPlateField.getText().trim().toLowerCase();

        ArrayList<AppointmentEntry> entries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(appointmentFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains("Technician Name: " + technicianName)) {

                    line = ensurePriorityExists(line);

                    String customerName = getValue(line, "Customer Name:");
                    String carPlate    = getValue(line, "Car Plate Number:");
                    String status      = getValue(line, "Appointment Status:");
                    String priority    = getValue(line, "Priority:");
                    String date        = getValue(line, "Appointment Date:");
                    String time        = getValue(line, "Appointment Time:");

                    boolean matchesStatus = selectedStatusFilter.equals("All")
                            || status.equalsIgnoreCase(selectedStatusFilter);
                    boolean matchesPlate  = plateSearch.isEmpty()
                            || carPlate.toLowerCase().contains(plateSearch);

                    if (matchesStatus && matchesPlate) {
                        AppointmentEntry entry = new AppointmentEntry();
                        entry.line         = line;
                        entry.customerName = customerName;
                        entry.carPlate     = carPlate;
                        entry.status       = status;
                        entry.priority     = priority;
                        entry.date         = date;
                        entry.time         = time;
                        entries.add(entry);
                    }
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading appointments file.");
        }

      
        entries.sort((a, b) -> {
            boolean aHigh = "High".equalsIgnoreCase(a.priority);
            boolean bHigh = "High".equalsIgnoreCase(b.priority);

            if (aHigh != bHigh) {
                return aHigh ? -1 : 1;
            }

            
            int dateCmp = b.date.compareTo(a.date);
            if (dateCmp != 0) return dateCmp;
            return b.time.compareTo(a.time);
        });

        // Populate table in sorted order
        for (AppointmentEntry entry : entries) {
            appointmentLines.add(entry.line);
            tableModel.addRow(new Object[]{
                    entry.customerName,
                    entry.carPlate,
                    entry.status,
                    "View Details"
            });
        }
    }

    private String ensurePriorityExists(String line) {
        if (!line.contains("Priority:")) {
            line = line + " | Priority: Normal";
        }
        return line;
    }

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

    private void showAppointmentDetails(int row) {
        if (row < 0 || row >= appointmentLines.size()) {
            return;
        }

        String line = appointmentLines.get(row);

        String appointmentID = getValue(line, "Appointment ID:");
        String customerName  = getValue(line, "Customer Name:");
        String service       = getValue(line, "Service:");
        String technician    = getValue(line, "Technician Name:");
        String carModel      = getValue(line, "Car Model:");
        String carPlate      = getValue(line, "Car Plate Number:");
        String date          = getValue(line, "Appointment Date:");
        String time          = getValue(line, "Appointment Time:");
        String status        = getValue(line, "Appointment Status:");
        String priority      = getValue(line, "Priority:");

        JDialog detailsDialog = new JDialog(this, "Appointment Details", true);
        detailsDialog.setSize(650, 800);
        detailsDialog.setLocationRelativeTo(this);
        detailsDialog.setLayout(new BorderLayout());

        JLabel dialogTitle = new JLabel("Appointment Details", JLabel.CENTER);
        dialogTitle.setFont(new Font("Arial", Font.BOLD, 22));
        dialogTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        detailsDialog.add(dialogTitle, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(10, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        detailsPanel.add(new JLabel("Appointment ID:"));
        detailsPanel.add(new JLabel(appointmentID));

        detailsPanel.add(new JLabel("Customer Name:"));
        detailsPanel.add(new JLabel(customerName));

        detailsPanel.add(new JLabel("Service:"));
        detailsPanel.add(new JLabel(service));

        detailsPanel.add(new JLabel("Technician Name:"));
        detailsPanel.add(new JLabel(technician));

        detailsPanel.add(new JLabel("Car Model:"));
        detailsPanel.add(new JLabel(carModel));

        detailsPanel.add(new JLabel("Licence Plate:"));
        detailsPanel.add(new JLabel(carPlate));

        detailsPanel.add(new JLabel("Appointment Date:"));
        detailsPanel.add(new JLabel(date));

        detailsPanel.add(new JLabel("Appointment Time:"));
        detailsPanel.add(new JLabel(time));

        detailsPanel.add(new JLabel("Appointment Status:"));
        JComboBox<String> statusBox = new JComboBox<>(new String[]{
                "Pending", "In Progress", "Completed"
        });
        statusBox.setSelectedItem(status);
        detailsPanel.add(statusBox);

        detailsPanel.add(new JLabel("Priority:"));
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{
                "Normal", "High"
        });
        priorityBox.setSelectedItem(priority.isEmpty() ? "Normal" : priority);
        detailsPanel.add(priorityBox);

        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));

        JLabel previousFeedbackLabel = new JLabel("Previous Feedback:");
        previousFeedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea previousFeedbackArea = new JTextArea(5, 30);
        previousFeedbackArea.setEditable(false);
        previousFeedbackArea.setLineWrap(true);
        previousFeedbackArea.setWrapStyleWord(true);
        previousFeedbackArea.setText(loadPreviousFeedback(appointmentID));

        JScrollPane previousFeedbackScroll = new JScrollPane(previousFeedbackArea);

        JLabel feedbackLabel = new JLabel("New Technician Feedback:");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextArea feedbackArea = new JTextArea(6, 30);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);

        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);

        JPanel feedbackInputPanel = new JPanel(new BorderLayout());
        feedbackInputPanel.add(feedbackLabel, BorderLayout.NORTH);
        feedbackInputPanel.add(feedbackScroll, BorderLayout.CENTER);

        JPanel previousFeedbackPanel = new JPanel(new BorderLayout());
        previousFeedbackPanel.add(previousFeedbackLabel, BorderLayout.NORTH);
        previousFeedbackPanel.add(previousFeedbackScroll, BorderLayout.CENTER);

        JPanel allFeedbackPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        allFeedbackPanel.add(previousFeedbackPanel);
        allFeedbackPanel.add(feedbackInputPanel);

        JPanel buttonPanel = new JPanel();

        JButton saveChangesButton = new JButton("Save Status & Priority");
        JButton saveFeedbackButton = new JButton("Save Feedback");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(saveChangesButton);
        buttonPanel.add(saveFeedbackButton);
        buttonPanel.add(closeButton);

        feedbackPanel.add(allFeedbackPanel, BorderLayout.CENTER);
        feedbackPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(feedbackPanel, BorderLayout.CENTER);

        detailsDialog.add(mainPanel, BorderLayout.CENTER);

        saveChangesButton.addActionListener(e -> {
            String selectedStatus   = statusBox.getSelectedItem().toString();
            String selectedPriority = priorityBox.getSelectedItem().toString();

            updateAppointmentStatusAndPriority(appointmentID, selectedStatus, selectedPriority);
            loadAssignedAppointments();

            JOptionPane.showMessageDialog(detailsDialog,
                    "Appointment updated successfully.\nStatus: " + selectedStatus + "\nPriority: " + selectedPriority);
        });

        saveFeedbackButton.addActionListener(e -> {
            String feedback = feedbackArea.getText().trim();

            if (feedback.isEmpty()) {
                JOptionPane.showMessageDialog(detailsDialog, "Please enter feedback before saving.");
                return;
            }

            saveFeedback(appointmentID, customerName, carPlate, feedback);

            previousFeedbackArea.setText(loadPreviousFeedback(appointmentID));
            feedbackArea.setText("");

            JOptionPane.showMessageDialog(detailsDialog, "Feedback saved successfully.");
        });

        closeButton.addActionListener(e -> detailsDialog.dispose());

        detailsDialog.setVisible(true);
    }

    private void updateAppointmentStatusAndPriority(String appointmentID, String newStatus, String newPriority) {
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(appointmentFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains("Appointment ID: " + appointmentID)) {

                    line = ensurePriorityExists(line);

                    line = line.replaceAll("Appointment Status: [^|]*", "Appointment Status: " + newStatus + " ");
                    line = line.replaceAll("Priority: .*", "Priority: " + newPriority);
                }

                updatedLines.add(line);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading appointments file.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(appointmentFile))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine.trim());
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating appointment.");
        }
    }

    private void saveFeedback(String appointmentID, String customerName, String carPlate, String feedback) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(feedbackFile, true))) {
            bw.write("Appointment ID: " + appointmentID
                    + " | Technician Name: " + technicianName
                    + " | Customer Name: " + customerName
                    + " | Car Plate Number: " + carPlate
                    + " | Date: " + timestamp
                    + " | Feedback: " + feedback);
            bw.newLine();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving feedback.");
        }
    }

    private String loadPreviousFeedback(String appointmentID) {
        File file = new File(feedbackFile);

        if (!file.exists()) {
            return "No previous feedback found.";
        }

        StringBuilder feedbackText = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(feedbackFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains("Appointment ID: " + appointmentID)) {
                    String date     = getValue(line, "Date:");
                    String feedback = getValue(line, "Feedback:");
                    if (!feedback.isEmpty()) {
                        if (!date.isEmpty()) {
                            feedbackText.append(date).append(" : ").append(feedback).append("\n");
                        } else {
                            feedbackText.append(feedback).append("\n");
                        }
                    }
                }
            }

        } catch (IOException e) {
            return "Error loading previous feedback.";
        }

        if (feedbackText.length() == 0) {
            return "No previous feedback found.";
        }

        return feedbackText.toString();
    }

    class AppointmentEntry {
        String line;
        String customerName;
        String carPlate;
        String status;
        String priority;
        String date;
        String time;
    }

    class PriorityRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
            );

            String line     = appointmentLines.get(row);
            String priority = getValue(line, "Priority:");

            if ("High".equalsIgnoreCase(priority)) {
                component.setBackground(new Color(255, 210, 210));
                component.setForeground(Color.BLACK);
            } else {
                component.setBackground(Color.WHITE);
                component.setForeground(Color.BLACK);
            }

            if (isSelected) {
                component.setBackground(new Color(180, 210, 255));
            }

            return component;
        }
    }


    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("View Details");
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            setText("View Details");

            String line     = appointmentLines.get(row);
            String priority = getValue(line, "Priority:");

            if ("High".equalsIgnoreCase(priority)) {
                setBackground(new Color(255, 120, 120));
            } else {
                setBackground(null);
            }

            return this;
        }
    }


    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            button = new JButton("View Details");

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    showAppointmentDetails(selectedRow);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column
        ) {
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "View Details";
        }
    }
}
