import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ppeTransactionRecords extends JFrame implements ActionListener {

	//Components
	private JTable table;
	private JLabel searchLabel = new JLabel("Search: ");
    private JButton searchbyItemCodeButton = new JButton("Search by Item Code");
    private JButton searchbyIDButton = new JButton("Search by ID");
    private JButton returnButton = new JButton("Return");
    private JTextField searchTextField = new JTextField();
	private Choice transactionTypeChoice = new Choice();
	private JLabel startDateLabel = new JLabel("Start Date:");
	private JTextField startDateTextField = new JTextField();
	private JLabel endDateLabel = new JLabel("End Date:");
	private JTextField endDateTextField = new JTextField();


	public ppeTransactionRecords() {
		//Page Styling
		setTitle("PPE Transaction Records");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 250);
		setLocationRelativeTo(null);

		//Table Declarations
		String[] columns = {"Transaction ID", "Item Code", "Quantity", "Transaction Type", "Hospital/Supplier Code", "Date", "Time" };
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		ppeTransactionUtils.loadTransactionData(model);
		add(new JScrollPane(table), "Center");

        //Text Field Styling
		searchTextField.setPreferredSize(new Dimension(100, 25));
		startDateTextField.setPreferredSize(new Dimension(100, 25));
		endDateTextField.setPreferredSize(new Dimension(100, 25));

        //Button Action Listeners & Styling
		searchbyItemCodeButton.addActionListener(this);
        searchbyIDButton.addActionListener(this);
        returnButton.addActionListener(this);
		searchbyItemCodeButton.setBackground(Color.LIGHT_GRAY);
        searchbyIDButton.setBackground(Color.LIGHT_GRAY);
        returnButton.setBackground(Color.LIGHT_GRAY);
        searchbyItemCodeButton.setFocusable(false);
        searchbyIDButton.setFocusable(false);
        returnButton.setFocusable(false);

		//Choice Box Declarations
		transactionTypeChoice.add("Transaction Type");
		transactionTypeChoice.add("Resupply");
		transactionTypeChoice.add("Distribution");

		//Adding to Panel
		JPanel panel = new JPanel();
		panel.add(searchLabel);
		panel.add(searchTextField);
		panel.add(searchbyItemCodeButton);
		panel.add(searchbyIDButton);
		panel.add(transactionTypeChoice);
		panel.add(startDateLabel);
		panel.add(startDateTextField);
		panel.add(endDateLabel);
		panel.add(endDateTextField);
		panel.add(returnButton);
		add(panel, "North");

		setVisible(true);
	}

	//Method Calling
	public static void main(String[] args) {
		new ppeTransactionRecords();
	}

	//Button Actions
	@Override
	public void actionPerformed(ActionEvent e) {
		String searchValue = searchTextField.getText().trim();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	
		//Search by Item Code Button
		if (e.getSource() == searchbyIDButton || e.getSource() == searchbyItemCodeButton) {
			int columnToSearch = (e.getSource() == searchbyIDButton) ? 0 : 1;
			String selectedTransactionType = transactionTypeChoice.getSelectedItem();
			model.setRowCount(0);

			String startDateInput = startDateTextField.getText().trim();
        	String endDateInput = endDateTextField.getText().trim();
        	LocalDate startDate = null;
        	LocalDate endDate = null;
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			//Date Range Filter Validation
			try {
				if (!startDateInput.isEmpty()) {
					startDate = LocalDate.parse(startDateInput, formatter);
				}
				if (!endDateInput.isEmpty()) {
					endDate = LocalDate.parse(endDateInput, formatter);
				}
			} catch (DateTimeParseException ex) {
				JOptionPane.showMessageDialog(this, "Invalid date format.\nExpected: dd/MM/yyyy");
				return; 
			}
	
			//Reading from file
			try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/transactions.txt"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(" / ");
					if (parts.length < 7) { continue; }
					String transactionID = parts[0].split(": ")[1];
					String itemCode = parts[1].split(": ")[1];
					String quantity = parts[2].split(": ")[1];
					String transactionType = parts[3].split(": ")[1];
                	String hsCode = parts[4].split(": ")[1];
                	String date = parts[5].split(": ")[1];
                	String time = parts[6].split(": ")[1];

					//General Validations
					boolean typeMatches = selectedTransactionType.equals("Transaction Type") || transactionType.equalsIgnoreCase(selectedTransactionType);
					String columnValue = (columnToSearch == 0) ? transactionID : itemCode;
					boolean searchMatches = columnValue.contains(searchValue);
					boolean dateInRange = true;

					//Date Range Filter Validation (2)
					try {
						LocalDate recordDate = LocalDate.parse(date, formatter);
						if (startDate != null && recordDate.isBefore(startDate)) {
							dateInRange = false;
						}
						if (endDate != null && recordDate.isAfter(endDate)) {
							dateInRange = false;
						}
					} catch (DateTimeParseException ex) {
						continue;
					}
	
					if (typeMatches && searchMatches && dateInRange) {
						model.addRow(new Object[]{transactionID, itemCode, quantity, transactionType, hsCode, date, time});
					}
				}
			} catch (IOException ex) {
				System.out.println("Error");
			}
		//Return Button
		} else if (e.getSource() == returnButton) {
			new ppeTransactions();
			dispose();
		}
	}
}
