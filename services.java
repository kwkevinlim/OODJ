import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class services extends JFrame implements ActionListener {

    //gui components
    private JButton editMajorServiceButton = new JButton("Edit Major Service Details");
    private JButton editMinorServiceButton = new JButton("Edit Minor Service Details");
    private JButton returnButton = new JButton("Return to Manager Page");
    private JButton saveChangesButton = new JButton("Save Changes");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel serviceNameLabel = new JLabel("Service Name:");
    private JLabel serviceIDLabel = new JLabel("Service ID:");
    private JLabel priceLabel = new JLabel("Price:");
    private JLabel durationLabel = new JLabel("Duration (Hours):");
    private JTextField priceField = new JTextField();
    private JTextField durationField = new JTextField();
    private JDialog editDialog = new JDialog(this, "Edit Service", true);
    private JTable table;
    private String serviceID = "";
    private String serviceName = "";

    public services() {
        //gui layout
        setTitle("Service Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 250);
        setLocationRelativeTo(null);

        String[] columns = { "Service ID", "Service Name", "Price", "Duration (Hours)" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        serviceUtilities.loadServiceData(model);
        add(new JScrollPane(table), "Center");

        editMajorServiceButton.addActionListener(this);
        editMinorServiceButton.addActionListener(this);
        returnButton.addActionListener(this);
        saveChangesButton.addActionListener(this);
        cancelButton.addActionListener(this);

        editMajorServiceButton.setBackground(Color.LIGHT_GRAY);
        editMinorServiceButton.setBackground(Color.LIGHT_GRAY);
        returnButton.setBackground(Color.LIGHT_GRAY);
        saveChangesButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setBackground(Color.LIGHT_GRAY);
        editMajorServiceButton.setFocusable(false);
        editMinorServiceButton.setFocusable(false);
        returnButton.setFocusable(false);
        saveChangesButton.setFocusable(false);
        cancelButton.setFocusable(false);

        JPanel panel = new JPanel();
        panel.add(editMajorServiceButton);
        panel.add(editMinorServiceButton);
        panel.add(returnButton);
        add(panel, "North");

        editDialog.setSize(400, 300);
        editDialog.setLayout(new GridLayout(5, 2));
        editDialog.add(serviceIDLabel);
        editDialog.add(serviceNameLabel);
        editDialog.add(priceLabel);
        editDialog.add(priceField);
        editDialog.add(durationLabel);
        editDialog.add(durationField);
        editDialog.add(saveChangesButton);
        editDialog.add(cancelButton);

        setVisible(true);
    }

    //display details based on service id
    public String displayServiceDetails(String serviceID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                if (line.contains("Service ID: " + serviceID + " |")) {
                    String[] parts = line.split(" \\| ");
                    priceField.setText(parts[2].split(": ")[1].trim());
                    durationField.setText(parts[3].split(": ")[1].trim());
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
        String price;
        String duration;
        //only price and duration are allowed to be edited
        if (e.getSource() == editMajorServiceButton) {
            serviceID = table.getValueAt(0, 0).toString();
            serviceName = table.getValueAt(0, 1).toString();
            serviceIDLabel.setText("Service ID: " + serviceID);
            serviceNameLabel.setText("Service Name: " + serviceName);
            displayServiceDetails(serviceID);
            editDialog.setVisible(true);
        } else if (e.getSource() == editMinorServiceButton) {
            serviceID = table.getValueAt(1, 0).toString();
            serviceName = table.getValueAt(1, 1).toString();
            displayServiceDetails(serviceID);
            serviceIDLabel.setText("Service ID: " + serviceID);
            serviceNameLabel.setText("Service Name: " + serviceName);
            editDialog.setVisible(true);
        } else if (e.getSource() == returnButton) {
            new managerPage();
            dispose();
        } else if (e.getSource() == saveChangesButton) {
            //saves updated price or duration
            price = priceField.getText();
            duration = durationField.getText();
            serviceUtilities.updateServiceDetails(serviceID, serviceName, price, duration);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            serviceUtilities.loadServiceData(model);
            editDialog.dispose();
        } else if (e.getSource() == cancelButton) {
            editDialog.dispose();
        }
    }

    public static void main(String[] args) {
        new services();
    }

}
