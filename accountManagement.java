import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class accountManagement extends JFrame {
	public accountManagement() {
		setTitle("Account Management");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 300);
		setLayout(new GridLayout(6, 1));

		JLabel titleLabel = new JLabel("Account Management", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(titleLabel);

		try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/role.txt"))) {
			String role = reader.readLine();
			if (role.equals("Manager")) {
				titleLabel.setText("Staff Management");
				String[] buttonLabels = { "View Staff", "Add Staff", "Delete Staff", "Update Staff Details", "Return" };
				for (String label : buttonLabels) {
					JButton button = new JButton(label);
					button.addActionListener(new ButtonClickListener());
					button.setFocusable(false);
					if (label.equals("Return")) {
						button.setBackground(Color.RED);
						button.setForeground(Color.WHITE);
					} else {
						button.setBackground(Color.LIGHT_GRAY);
					}
					add(button);
				}

				setLocationRelativeTo(null);
				setVisible(true);
			} else {
				titleLabel.setText(role + " Customer Management");
				String[] buttonLabels = { "View Customers", "Add Customer", "Delete Customer",
						"Update Customer Details", "Return" };
				for (String label : buttonLabels) {
					JButton button = new JButton(label);
					button.addActionListener(new ButtonClickListener());
					button.setFocusable(false);
					if (label.equals("Return")) {
						button.setBackground(Color.RED);
						button.setForeground(Color.WHITE);
					} else {
						button.setBackground(Color.LIGHT_GRAY);
					}
					add(button);
				}

				setLocationRelativeTo(null);
				setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String role = "";
			try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/role.txt"))) {
				role = reader.readLine();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			JButton source = (JButton) e.getSource();
			if ((source.getText()).equals("View Staff") || (source.getText()).equals("View Customers")) {
				new usersDatabase();
			} else if ((source.getText()).equals("Add Staff") || (source.getText()).equals("Add Customer")) {
				new addNewUser();
			} else if ((source.getText()).equals("Delete Staff") || (source.getText()).equals("Delete Customer")) {
				new deleteUsers();
			} else if ((source.getText()).equals("Update Staff Details") || (source.getText()).equals("Update Customer Details")) {
				new accountUpdate();
			} else if ((source.getText()).equals("Return")) {
				if (role.equals("Manager")) {
					new managerPage();
				} else {
					new csPage();
				}
			}
			dispose();
		}
	}

	public static void main(String[] args) {
		new accountManagement();
	}
}