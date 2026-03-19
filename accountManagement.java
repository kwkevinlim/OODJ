import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class accountManagement extends JFrame {
	public accountManagement() {
		setTitle("Account Management");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 300);
		setLayout(new GridLayout(6, 1));

		JLabel titleLabel = new JLabel("Account Management", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(titleLabel);

		String[] buttonLabels = {"View Users", "Add Users", "Delete Users", "Update User Details", "Return"};
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

	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if ((source.getText()).equals("View Users")) {
				new usersDatabase();
			} else if ((source.getText()).equals("Add Users")) {
				new addNewUser();
			} else if ((source.getText()).equals("Delete Users")) {
				new deleteUsers();
			} else if ((source.getText()).equals("Update User Details")) {
				new accountUpdate();
			} else if ((source.getText()).equals("Return")) {
				new adminPage();
			}
			dispose();
		}
	}

	public static void main(String[] args) {
		new accountManagement();
	}
}