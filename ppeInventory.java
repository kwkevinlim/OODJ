import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.*;

public class ppeInventory extends JFrame implements ActionListener {

	//Components
	private JTable table;
    private JButton returnButton = new JButton("Return");

	//Loadint table data
	public static void loadInventoryData(DefaultTableModel model) {
		try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/ppe.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] parts = line.split(" / ");
				String itemCode = parts[0].split(": ")[1];
				String itemName = parts[1].split(": ")[1];
				String Quantity = parts[2].split(": ")[1];
				String Suppliers = parts[3].split(": ")[1];
				model.addRow(new Object[]{itemCode, itemName, Quantity, Suppliers});
			}
		} catch (IOException e) {
			System.out.println("Error");
		}
	}


	public ppeInventory() {
		//Page Styling
		setTitle("PPE Inventory");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 250);
		setLocationRelativeTo(null);

		//Table Declarations
		String[] columns = {"Item Code", "Item Name", "Quantity(Boxes)", "Supplier(s)"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		table.getColumnModel().getColumn(2).setCellRenderer(new QuantityCellRenderer());
		loadInventoryData(model);
		add(new JScrollPane(table), "Center");

		//Button action listener & Styling
		returnButton.addActionListener(this);
        returnButton.setBackground(Color.LIGHT_GRAY);
		returnButton.setFocusable(false);

		//Adding to JPanel
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(returnButton);
		add(panel, "North");

		setVisible(true);
	}

	//Method Calling
	public static void main(String[] args) {
		new ppeInventory();
	}

	//Button Actions
	@Override
	//Return Button
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == returnButton) {
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
					new ppeInventory();
				}
				role.close();
				dispose();
			} catch(FileNotFoundException f) {
				f.printStackTrace();
				JOptionPane.showMessageDialog (null, "File not found.");
				new ppeInventory();
			}
			dispose();
		}
	}

	//Table cell changes colour based on quantity
	class QuantityCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component cell = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        try {
            int quantity = Integer.parseInt(value.toString());

            if (quantity <= 10) {
                cell.setBackground(Color.RED);
                cell.setForeground(Color.WHITE);
            } else if (quantity <= 25) {
                cell.setBackground(Color.ORANGE);
                cell.setForeground(Color.BLACK);
            } else {
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            cell.setBackground(Color.WHITE);
            cell.setForeground(Color.BLACK);
        }

        return cell;
    }
	}
}
