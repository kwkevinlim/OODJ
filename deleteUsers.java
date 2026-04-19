import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

public class deleteUsers extends JFrame implements ActionListener {

	private JTable table;
	private JLabel searchLabel = new JLabel("Enter: ");
    private JButton deletebyNameButton = new JButton("Delete by Name");
    private JButton deletebyIDButton = new JButton("Delete by ID");
    private JButton returnButton = new JButton("Return");
    private JTextField searchTextField = new JTextField();


	public deleteUsers() {
		setTitle("User Database");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 250);
		setLocationRelativeTo(null);

		String[] columns = {"User ID", "Username", "Password", "User Role"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		userUtilities.loadUserData(model);
		add(new JScrollPane(table), "Center");

        searchTextField.setPreferredSize(new Dimension(100, 25));

        deletebyNameButton.addActionListener(this);
        deletebyIDButton.addActionListener(this);
        returnButton.addActionListener(this);

        deletebyNameButton.setBackground(Color.LIGHT_GRAY);
        deletebyIDButton.setBackground(Color.LIGHT_GRAY);
        returnButton.setBackground(Color.RED);
        returnButton.setForeground(Color.WHITE);
        deletebyNameButton.setFocusable(false);
        deletebyIDButton.setFocusable(false);
        returnButton.setFocusable(false);

		JPanel panel = new JPanel();
		panel.add(searchLabel);
		panel.add(searchTextField);
		panel.add(deletebyNameButton);
		panel.add(deletebyIDButton);
		panel.add(returnButton);
		add(panel, "North");

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource()==returnButton) {
            new accountManagement();
            dispose();
        }
        String search = searchTextField.getText().trim();
        if (search.isEmpty()) return;

        try {
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
                String line;
                while ((line = br.readLine()) != null) lines.add(line);
            }

            boolean found = false;
            try (FileWriter fw = new FileWriter("txtfiles/users.txt")) {
                for (String line : lines) {
                    if (line.trim().isEmpty()) {
                        fw.write("\n");
                        continue;
                    }
                    boolean match = false;
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split(" | ");
                        if (parts.length >= 4) {
                            String id = parts[0].split(": ")[1].trim();
                            String name = parts[1].split(": ")[1].trim();
                            match = e.getSource() == deletebyIDButton 
                                ? id.equals(search) 
                                : name.equalsIgnoreCase(search);
                        }
                    }
                    
                    fw.write(match ? "\n" : line + "\n");
                    if (match) found = true;
                }
            }

            if (found) {
                JOptionPane.showMessageDialog(this, "User/UserID "+search+" deleted.");
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                UserUtils.loadUserData(model);
                searchTextField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "User not found");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
	}

    public static void main(String[] args) {
		new deleteUsers();
	}
}
