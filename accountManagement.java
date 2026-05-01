import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class accountManagement extends JFrame {
	public accountManagement() {
		//gui compoenents and layout
		setTitle("Account Management");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 300);
		setLayout(new GridLayout(6, 1));

		JLabel titleLabel = new JLabel("Account Management", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(titleLabel);
		//role reader to decide which button list to use
		try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/role.txt"))) {
			String role = reader.readLine();
			//manager can only manage staff members
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
				//customer accounts will be managed by the counter staff, or should they also be managed by managers?
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
			JOptionPane.showMessageDialog(null, "Can't locate users.txt. Please try again later. :(");
			dispose();
		}

	}

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//declarations and method to determine user role
			String role = "";
			try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/role.txt"))) {
				role = reader.readLine();
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Can't locate users.txt. Please try again later. :(");
				dispose();
			}
			//button actions, mostly opening new pages only
			JButton source = (JButton) e.getSource();
			if ((source.getText()).equals("View Staff") || (source.getText()).equals("View Customers")) {
				//page that displays all users in table form, kind of like database but data is read from a text file instead
				new usersDatabase();
			} else if ((source.getText()).equals("Add Staff") || (source.getText()).equals("Add Customer")) {
				//page to register new staff members or customers
				new addNewUser();
			} else if ((source.getText()).equals("Delete Staff") || (source.getText()).equals("Delete Customer")) {
				//helper class to delete users
				new deleteUsers();
			} else if ((source.getText()).equals("Update Staff Details") || (source.getText()).equals("Update Customer Details")) {
				//page for manager to update staff details OR counter staff to update customer info
				new accountUpdate();
			} else if ((source.getText()).equals("Return")) {
				if (role.equals("Manager")) {
					//manager page if role is manager
					new managerPage();
				} else {
					//counter staff page if role is counter staff
					new csPage();
				}
			}
			dispose();
		}
	}

	//main method to summon the page when the Account Management button is clicked
	public static void main(String[] args) {
		new accountManagement();
	}
}