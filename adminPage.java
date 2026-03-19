import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminPage extends JFrame {
	public adminPage() {
		//Page Styling
		setTitle("Admin Page");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 300);
		setLayout(new GridLayout(6, 1));

		//Components
		JLabel titleLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(titleLabel);

		String[] buttonLabels = {"PPE Inventory", "Transaction System", "View Supplier Details", "View Hospital Details", "Manage Accounts"};
		for (String label : buttonLabels) {
			JButton button = new JButton(label);
			button.addActionListener(new ButtonClickListener());
            button.setFocusable(false);
            button.setBackground(Color.LIGHT_GRAY);
			add(button);
		}

		setLocationRelativeTo(null);
		setVisible(true);
	}

	//Button Actions
	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if ((source.getText()).equals("PPE Inventory")) {
				new ppeInventory().setVisible(true);
			} else if ((source.getText()).equals("Transaction System")) {
				new ppeTransactions().setVisible(true);
			} else if ((source.getText()).equals("View Supplier Details")) {
				new supplierDatabase().setVisible(true);
			} else if ((source.getText()).equals("View Hospital Details")) {
				new hospitalDatabase().setVisible(true);
			} else if ((source.getText()).equals("Manage Accounts")) {
				new accountManagement().setVisible(true);
			}
			dispose();
		}
	}

	//Method Calling
	public static void main(String[] args) {
		new adminPage();
	}
}