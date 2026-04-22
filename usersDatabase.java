import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

public class usersDatabase extends JFrame implements ActionListener {

	private JTable table;

	private JLabel searchLabel = new JLabel("Search: ");
    private JButton searchbyNameButton = new JButton("Search by Name");
    private JButton searchbyIDButton = new JButton("Search by ID");
    private JButton returnButton = new JButton("Return");
    private JTextField searchTextField = new JTextField();
	private Choice roleChoice = new Choice();


	public usersDatabase() {
		setTitle("User Database");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 250);
		setLocationRelativeTo(null);

		String[] columns = {"User ID", "Username", "Password", "User Role"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		if (userUtilities.getUserRole().equals("Manager")) {
			userUtilities.loadStaffData(model);
		} else {
			userUtilities.loadCustomerData(model);
		}
		add(new JScrollPane(table), "Center");

        searchTextField.setPreferredSize(new Dimension(100, 25));

        searchbyNameButton.addActionListener(this);
        searchbyIDButton.addActionListener(this);
        returnButton.addActionListener(this);

        searchbyNameButton.setBackground(Color.LIGHT_GRAY);
        searchbyIDButton.setBackground(Color.LIGHT_GRAY);
        returnButton.setBackground(Color.LIGHT_GRAY);
        searchbyNameButton.setFocusable(false);
        searchbyIDButton.setFocusable(false);
        returnButton.setFocusable(false);

		roleChoice.add("View All");
		roleChoice.add("Managers");
		roleChoice.add("Counter Staffs");
		roleChoice.add("Technicians");
		roleChoice.add("Customers");

		JPanel panel = new JPanel();
		panel.add(searchLabel);
		panel.add(searchTextField);
		panel.add(searchbyNameButton);
		panel.add(searchbyIDButton);
		panel.add(roleChoice);
		panel.add(returnButton);
		add(panel, "North");

		setVisible(true);
	}

	public static void main(String[] args) {
		new usersDatabase();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String searchValue = searchTextField.getText().trim().toLowerCase();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	
		if (e.getSource() == searchbyIDButton || e.getSource() == searchbyNameButton) {
			int columnToSearch = (e.getSource() == searchbyIDButton) ? 0 : 1;
			String selectedRole = roleChoice.getSelectedItem();
	
			model.setRowCount(0);
	
			try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(" \\| ");
					if (parts.length < 4) { continue; }
					String userId = parts[0].split(": ")[1];
					String username = parts[1].split(": ")[1];
					String password = parts[2].split(": ")[1];
					String role = parts[3].split(": ")[1];

					boolean roleMatches = selectedRole.equals("View All") || role.equalsIgnoreCase(selectedRole);

					String columnValue = (columnToSearch == 0) ? userId : username;
					boolean searchMatches = columnValue.toLowerCase().contains(searchValue);
	
					if (roleMatches && searchMatches) {
						model.addRow(new Object[]{userId, username, password, role});
					}
				}
			} catch (IOException ex) {
				System.out.println("Error");
			}
		} else if (e.getSource() == returnButton) {
			new accountManagement();
			dispose();
		}
	}
}
