import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class addTransactions implements ActionListener {
    //Components
    private JFrame frame = new JFrame();
    private JButton addButton = new JButton("Add");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel messageLabel = new JLabel("Add a Transaction");
    private JLabel hospitalSupplierLabel = new JLabel("Supplier/Hospital Code: ");
    private JLabel quantityLabel = new JLabel("Quantity:");
    private JLabel itemCodeLabel = new JLabel("Item Code:");
    private JLabel typeLabel = new JLabel("Transaction Type:");
    private JLabel dateLabel = new JLabel("Date (dd/mm/yyyy): ");
    private JLabel timeLabel = new JLabel("Time (hh:mm): ");
    private JComboBox<String> typeComboBox = new JComboBox<>();
    private JComboBox<String> hospitalSupplierCodeComboBox = new JComboBox<>();
    private JComboBox<String> itemCodeComboBox = new JComboBox<>();
    private JTextField quantityField = new JTextField();
    private JTextField dateField = new JTextField();
    private JTextField timeField = new JTextField();

    //Arrays used for filtering mechanism later
    private String[] supplierCodes = {"S01", "S02", "S03", "S04"};
    private String[] hospitalCodes = {"H01", "H02", "H03", "H04"};
    private String[] S01Items = {"FS", "HC"};
    private String[] S02Items = {"GL", "MS"};
    private String[] S03Items = {"GW", "SC"};
    private String[] fullItemList = {"HC", "FS", "MS", "GL", "GW", "SC"};


    public addTransactions() {

        //Frame Styling
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        //Component Styling
        messageLabel.setBounds(50, 20, 300, 30);
        typeLabel.setBounds(50, 70, 150, 25);
        hospitalSupplierLabel.setBounds(50, 110, 150, 25);
        itemCodeLabel.setBounds(50, 150, 150, 25);
        quantityLabel.setBounds(50, 190, 150, 25);
        dateLabel.setBounds(50, 230, 150, 25);
        timeLabel.setBounds(50, 270, 150, 25);
        typeComboBox.setBounds(220, 70, 100, 25);
        hospitalSupplierCodeComboBox.setBounds(220, 110, 100, 25);
        itemCodeComboBox.setBounds(220, 150, 100, 25);
        quantityField.setBounds(220, 190, 100, 25);
        dateField.setBounds(220, 230, 100, 25);
        timeField.setBounds(220, 270, 100, 25);
        addButton.setBounds(110, 310, 100, 30);
        cancelButton.setBounds(220, 310, 100, 30);

        //Combo box declarations
        typeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Type", "Distribution", "Resupply"}));
        itemCodeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Item Code"}));
        hospitalSupplierCodeComboBox.addItem("Code");

        //Filter for hospital/supplier combo box depending on which is chosen
        typeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                hospitalSupplierCodeComboBox.removeAllItems();
                hospitalSupplierCodeComboBox.addItem("Code");

                if (selectedType.equals("Resupply")) {
                    for (String supplier : supplierCodes) {
                        hospitalSupplierCodeComboBox.addItem(supplier);
                    }
                } else if (selectedType.equals("Distribution")) {
                    for (String hospital : hospitalCodes) {
                        hospitalSupplierCodeComboBox.addItem(hospital);
                    }
                }
            }
        });

        //Filter for item code combo box based on whether hospital or supplier is chosen
        hospitalSupplierCodeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedObject = hospitalSupplierCodeComboBox.getSelectedItem();
                String selectedHS = (selectedObject != null) ? selectedObject.toString() : "";
                itemCodeComboBox.removeAllItems();
                itemCodeComboBox.addItem("Item Code");

                if ("S01".equals(selectedHS)) {
                    for (String items : S01Items) {
                        itemCodeComboBox.addItem(items);
                    }
                } else if ("S02".equals(selectedHS)) {
                    for (String items : S02Items) {
                        itemCodeComboBox.addItem(items);
                    }
                } else if ("S03".equals(selectedHS)) {
                    for (String items : S03Items) {
                        itemCodeComboBox.addItem(items);
                    }
                } else {
                    for (String items : fullItemList) {
                        itemCodeComboBox.addItem(items);
                        }
                }
            }});

        //Cosmetic styling
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        addButton.setBackground(new Color(0, 180, 0));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this);
        addButton.setFocusable(false);
        cancelButton.setBackground(new Color(255, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);

        //Adding to frame
        frame.add(messageLabel);
        frame.add(addButton);
        frame.add(cancelButton);
        frame.add(typeLabel);
        frame.add(hospitalSupplierLabel);
        frame.add(quantityLabel);
        frame.add(itemCodeLabel);
        frame.add(dateLabel);
        frame.add(timeLabel);
        frame.add(quantityField);
        frame.add(dateField);
        frame.add(timeField);
        frame.add(itemCodeComboBox);
        frame.add(hospitalSupplierCodeComboBox);
        frame.add(typeComboBox);
        frame.setVisible(true);
    }

    //Button actions
    @Override
    //Add Button
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String transactionType = (String) typeComboBox.getSelectedItem();
            String hsCode = (String) hospitalSupplierCodeComboBox.getSelectedItem();
            String itemCode = (String) itemCodeComboBox.getSelectedItem();
            String quantity = quantityField.getText();
            String date = dateField.getText();
            String time = timeField.getText();

            
            //Validations
            if (quantity.contains(" ") || date.contains(" ") || time.contains(" ")){
                JOptionPane.showMessageDialog(frame,"Quantity/Date/Time cannot contain spaces.");
                return;
            } else if (quantity.isEmpty() || 
                       date.isEmpty() || 
                       time.isEmpty() || 
                       itemCode.equals("Item Code") ||
                       transactionType.equals("Type") ||
                       hsCode.equals("Code")) {
                JOptionPane.showMessageDialog(frame,"All fields must be filled out.");
                return;
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            dateFormat.setLenient(false);
            timeFormat.setLenient(false);

            try {
                dateFormat.parse(date);
                timeFormat.parse(time);
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(frame, "Invalid date or time format.\nExpected:\nDate: dd/MM/yyyy\nTime: HH:mm");
                return;
            }

            //Writing to Files (Record Transaction & Update Inventory)
            int itemQuantity = Integer.parseInt(quantity);
            boolean inventoryUpdated = ppeTransactionUtils.updateInventory(itemCode, itemQuantity, transactionType, frame);
            if (inventoryUpdated) {
                ppeTransactionUtils.saveTransaction(itemCode, quantity, transactionType, hsCode, date, time, frame);
                typeComboBox.setSelectedIndex(0);
                hospitalSupplierCodeComboBox.setSelectedIndex(0);
                itemCodeComboBox.setSelectedIndex(0);
                quantityField.setText("");
                dateField.setText("");
                timeField.setText("");
            } else {
                return;
            }
            
        //Cancel Button
        } else if (e.getSource() == cancelButton) {
            new ppeTransactions();
            frame.dispose();
        }
    }

    //Method Calling
    public static void main(String[] args) {
        new addTransactions();
    }
}
