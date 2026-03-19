import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class ppeTransactions extends JFrame { 

    public ppeTransactions() {
		//Page Styling
		setTitle("PPE Transaction Page");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(350, 250);
		setLayout(new GridLayout(6, 1));
        setResizable(false);

		//Components
		JLabel titleLabel = new JLabel("PPE Transaction Management", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(titleLabel);

		String[] buttonLabels = {"View All Transaction Records", "Add a New Record", "Return"};
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
		//Button Actions
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if ((source.getText()).equals("View All Transaction Records")) {
				new ppeTransactionRecords().setVisible(true);
			} else if ((source.getText()).equals("Add a New Record")) {
				new addTransactions();
			} else if ((source.getText()).equals("Return")) {
				//how do i access the stored role variable?
				try {
					Scanner role = new Scanner(new File("txtfiles/role.txt"));
					if (role.hasNext()) {
						String userRole = role.next();
						if (userRole.equals("Admin")) {
							new adminPage().setVisible(true);
						} else if (userRole.equals("Staff")) {
							new staffPage().setVisible(true);
						} 
					} else {
						JOptionPane.showMessageDialog (null, "File not found.");
						new ppeTransactions().setVisible(true);
					}
					role.close();
				} catch(FileNotFoundException f) {
					f.printStackTrace();
					JOptionPane.showMessageDialog (null, "File not found.");
					new ppeTransactions().setVisible(true);
				}
			} 
			dispose();
		}
	}

	//Method Calling
	public static void main(String[] args) {
		new ppeTransactions();
	}
}