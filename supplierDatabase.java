import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Scanner;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;

public class supplierDatabase extends JFrame implements ActionListener {

	//Components
	private JTable table;
    private JButton returnButton = new JButton("Return");
	private JButton updateLinkButton = new JButton("Update Details");

	public static void loadSupplierData(DefaultTableModel model) {
		//Loading Table Data
		try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/supplierDetails.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] parts = line.split(" / ");
				String supplierCode = parts[0].split(": ")[1];
				String supplierName = parts[1].split(": ")[1];
				String supplierAddress = parts[2].split(": ")[1];
				String supplierPhoneNumber = parts[3].split(": ")[1];
				model.addRow(new Object[]{supplierCode, supplierName, supplierAddress, supplierPhoneNumber});
			}
		} catch (IOException e) {
			System.out.println("Error");
		}
	}


	public supplierDatabase() {
		//Frame Styling
		setTitle("Supplier Database");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(850, 250);
		setLocationRelativeTo(null);

		//Table Design
		String[] columns = {"Supplier Code", "Name", "Address", "Phone Number"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(250);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		loadSupplierData(model);
		add(new JScrollPane(table), "Center");

		boolean hideUpdateButton = false;

		try {
			Scanner role = new Scanner(new File("txtfiles/role.txt"));
			if (role.hasNext()) {
				String userRole = role.next();
				if (userRole.equals("Admin")) {
					hideUpdateButton = true;
				} else if (userRole.equals("Staff")) {
					hideUpdateButton = false;
				} 
			}
			role.close();
		} catch(FileNotFoundException f) {
			JOptionPane.showMessageDialog (null, "File not found.");
		}

		//Button Action Listeners
		updateLinkButton.addActionListener(this);
		returnButton.addActionListener(this);

		//Button & Label Styling
		updateLinkButton.setBackground(Color.LIGHT_GRAY);
        returnButton.setBackground(Color.LIGHT_GRAY);
		updateLinkButton.setFocusable(false);
		returnButton.setFocusable(false);

		//Hide Button Function (Continued)
		updateLinkButton.setVisible(hideUpdateButton);

		//Adding to Panel
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(updateLinkButton);
		panel.add(returnButton);
		add(panel, "North");

		setVisible(true);
	}

	//Method Calling
	public static void main(String[] args) {
		new supplierDatabase();
	}

	//Button Actions
	@Override
	//Update Button
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateLinkButton) {
			new supplierUpdater();
			dispose();
		//Cancel Button
		} else{
			try {
					Scanner role = new Scanner(new File("txtfiles/role.txt"));
					if (role.hasNext()) {
						String userRole = role.next();
						if (userRole.equals("Admin")) {
							new adminPage().setVisible(true);;
						} else if (userRole.equals("Staff")) {
							new staffPage().setVisible(true);;
						} 
					} else {
						JOptionPane.showMessageDialog (null, "File not found.");
						new supplierDatabase();
					}
					role.close();
					dispose();
				} catch(FileNotFoundException f) {
					f.printStackTrace();
					JOptionPane.showMessageDialog (null, "File not found.");
					new supplierDatabase();
				}
		}
	}
}
