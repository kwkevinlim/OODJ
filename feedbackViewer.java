import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class feedbackViewer implements ActionListener {
    private JFrame frame = new JFrame("Feedback Viewer");
    private JTable feedbackTable;
    private JLabel titleLabel = new JLabel("Appointment Feedback");
    private JButton returnButton = new JButton("Return to Main Page");

    public feedbackViewer() {

        String[] columnNames = { "Feedback ID", "Appointment ID", "Customer ID", "Customer Rating", "Customer Feedback", "Technician ID", "Technician Feedback" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        feedbackTable = new JTable(model);
        loadFeedback(model);
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        scrollPane.setBounds(20, 60, 760, 480);

        frame.setLayout(null);
        frame.add(titleLabel);
        frame.add(returnButton);
        frame.add(scrollPane);
        frame.setVisible(true);


        returnButton.addActionListener(this);
        returnButton.setBounds(20, 20, 150, 30);
        returnButton.setFocusable(false);

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(300, 20, 200, 30);
        feedbackTable.setRowHeight(30);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }


    public static void loadFeedback(DefaultTableModel model) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/feedback.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;
                String[] parts = line.split(" \\| ");
                String feedbackID = parts[0].split(": ")[1].trim();
                String appointmentID = parts[1].split(": ")[1].trim();
                String customerID = parts[2].split(": ")[1].trim();
                String customerRating = parts[3].split(": ")[1].trim();
                String customerFeedback = parts[4].split(": ")[1].trim();
                String technicianID = parts[5].split(": ")[1].trim();
                String technicianFeedback = parts[6].split(": ")[1].trim();
                model.addRow(new Object[] { feedbackID, appointmentID, customerID, customerRating, customerFeedback, technicianID, technicianFeedback });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading file.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnButton) {
        frame.dispose();
        new managerPage();
    }
    }

    public static void main(String[] args) {
        new feedbackViewer();
    }
}
