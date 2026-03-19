import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class staffPage extends JFrame {
	public staffPage() {
		//Page styling
		setTitle("Staff Page");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 300);
		setLayout(new GridLayout(6, 1));

		//Components & Styling
		JLabel titleLabel = new JLabel("Staff Panel", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(titleLabel);

		//Table declarations
		String[] buttonLabels = {"PPE Inventory", "Transaction System", "View Supplier Details", "View Hospital Details"};
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

	private class ButtonClickListener implements ActionListener {
		//Button Actions
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
			} 
			dispose();
		}
	}

	public static void main(String[] args) {
		new staffPage();
	}
}
