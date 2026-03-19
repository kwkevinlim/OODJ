import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class supplierUpdater implements ActionListener {
    // Frame Components
    private JFrame frame = new JFrame();
    private JButton updateButton = new JButton("Update");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel messageLabel = new JLabel("Update Supplier Details");
    private JLabel supplierCodeLabel = new JLabel("Supplier Code: ");
    private JLabel aspectLabel = new JLabel("Aspect to Update:");
    private JLabel newValueLabel = new JLabel("New Value(s): ");
    private JComboBox<String> supplierCodeComboBox = new JComboBox<>();
    private JComboBox<String> aspectComboBox = new JComboBox<>();
    private JTextField newValueField = new JTextField();

    public supplierUpdater() {

        // Frame Styling
        frame.setTitle("Update Supplier Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Component Styling
        messageLabel.setBounds(50, 20, 300, 30);
        supplierCodeLabel.setBounds(50, 70, 150, 25);
        aspectLabel.setBounds(50, 110, 150, 25);
        newValueLabel.setBounds(50, 150, 150, 25);
        supplierCodeComboBox.setBounds(220, 70, 100, 25);
        aspectComboBox.setBounds(220, 110, 120, 25);
        newValueField.setBounds(220, 150, 120, 25);
        updateButton.setBounds(110, 190, 100, 30);
        cancelButton.setBounds(220, 190, 100, 30);

        // Combo Box Declarations
        supplierCodeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Code", "S01", "S02", "S03"}));
        aspectComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Please Select", "Name", "Address", "Phone Number"}));

        // Button & Label Styling
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        updateButton.setBackground(new Color(0, 180, 0));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(this);
        updateButton.setFocusable(false);
        cancelButton.setBackground(new Color(255, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);

        // Adding to Frame
        frame.add(messageLabel);
        frame.add(updateButton);
        frame.add(cancelButton);
        frame.add(supplierCodeLabel);
        frame.add(supplierCodeComboBox);
        frame.add(aspectLabel);
        frame.add(aspectComboBox);
        frame.add(newValueLabel);
        frame.add(newValueField);
    }

    // Button Actions
    @Override
    //Update Button
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            String supplierCode = (String) supplierCodeComboBox.getSelectedItem();
            String aspect = (String) aspectComboBox.getSelectedItem();
            String newValue = (String) newValueField.getText();

            //Validations
            if (supplierCode.equals("Code") || aspect.equals("Please Select")) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled out.");
                return;
            } else if (newValue.equals("") || newValue.equals(" ")){
                JOptionPane.showMessageDialog(frame, "All fields must be filled out.");
                return;
            } else {
                //Input Checking
            try (BufferedReader br = new BufferedReader(new FileReader("txtfiles/supplierDetails.txt"))) {
                String line;
                java.util.List<String> lines = new java.util.ArrayList<>();

                while ((line = br.readLine()) != null) {
                    if (line.startsWith("Code: " + supplierCode + " ")) {
                        String[] parts = line.split(" / ");
                        String code = parts[0]; 
                        String name = parts[1]; 
                        String address = parts[2]; 
                        String phone = parts[3]; 

                        switch (aspect) {
                            case "Name":
                                name = "Name: " + newValue;
                                break;
                            case "Address":
                                address = "Address: " + newValue;
                                break;
                            case "Phone Number":
                                phone = "Phone Number: " + newValue;
                                break;
                            }

                        String updatedLine = String.join(" / ", code, name, address, phone);
                        lines.add(updatedLine);

                    }else{
                        lines.add(line);
                    }
                }

                //File Writing
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("txtfiles/supplierDetails.txt"))) {
                    for (String updatedLine : lines) {
                        bw.write(updatedLine);
                        bw.newLine();
                    }
                    JOptionPane.showMessageDialog(frame, "Supplier details updated successfully!");
                    supplierCodeComboBox.setSelectedIndex(0);
                    aspectComboBox.setSelectedIndex(0);
                    newValueField.setText("");
                    supplierCodeComboBox.setSelectedIndex(0);
                    aspectComboBox.setSelectedIndex(0);
                    newValueField.setText("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving changes to file.");
                }
                
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            }
        }else if(e.getSource() == cancelButton){
        new supplierDatabase();
        frame.dispose();
        }
    }

    // Method Calling
    public static void main(String[] args) {
        new supplierUpdater();
    }
}
