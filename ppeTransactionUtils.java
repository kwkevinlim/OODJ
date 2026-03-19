import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ppeTransactionUtils {
    
	//Writing to File
    public static void saveTransaction(String itemCode, String quantity, String transactionType, String hsCode, String date, String time, JFrame frame) {
		List<String> lines = new ArrayList<>();
		boolean filledBlank = false;
		int transactionID = 1;
		String line;
		
		try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/transactions.txt"))) {
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty() && !filledBlank) {
					lines.add("Transaction ID: " + transactionID + " / Item Code: " + itemCode + " / Quantity: " + quantity + " / Transaction Type: " + transactionType + 
								" / Hospital/Supplier Code: " + hsCode + " / Date: " + date + " / Time: " + time);
					filledBlank = true;
				} else {
					lines.add(line);
				}
				if (!line.trim().isEmpty()) {
					transactionID++;
				}
			}
			if (!filledBlank) {
				lines.add("Transaction ID: " + transactionID + " / Item Code: " + itemCode + " / Quantity: " + quantity + " / Transaction Type: " + transactionType + 
								" / Hospital/Supplier Code: " + hsCode + " / Date: " + date + " / Time: " + time);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Error reading file.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/transactions.txt"))) {
			for (String currentline : lines) {
				writer.write(currentline);
				writer.newLine();
			}
			JOptionPane.showMessageDialog(frame, "Transaction ID: " + transactionID + "\nItem Code: " + itemCode + "\nQuantity: " + quantity + "\nTransaction Type: " + transactionType + 
								"\nHospital/Supplier Code: " + hsCode + "\nDate: " + date + "\nTime: " + time, "Transaction successfully recorded!", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Error writing file.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Updating invetory & stock checking
	public static boolean updateInventory (String itemCode, int itemQuantity, String transactionType, JFrame frame) {
		List<String> updatedInventory = new ArrayList<>();
		boolean itemFound = false;
		boolean sufficientStock = true;

		try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/ppe.txt"))) {
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) {
					updatedInventory.add(line);
					continue;
				}

				String[] parts = line.split(" / ");
				String currentItemCode = parts[0].split(": ")[1];
				int quantityInStock = Integer.parseInt(parts[2].split(": ")[1]);

				if (currentItemCode.equals(itemCode)) {
					itemFound = true;

					if (transactionType.equals("Distribution")) {
						if (quantityInStock < itemQuantity) {
							sufficientStock = false;
							break;
						} else {
							int newQuantity = quantityInStock - itemQuantity;
							parts[2] = "Quantity: " + newQuantity;
						}
					}else if (transactionType.equals("Resupply")) {
						int newQuantity = quantityInStock + itemQuantity;
						parts[2] = "Quantity: " + newQuantity;
					}

					updatedInventory.add(String.join(" / ", parts));
				} else {
					updatedInventory.add(line);
				}
			}

			if (!itemFound) {
				JOptionPane.showMessageDialog(frame, "Item not found.");
				return false;
			}

			if (!sufficientStock) {
				JOptionPane.showMessageDialog(frame, "Insuffiecient stock to perform distribution, please resupply.");
				return false;
			}


		} catch (IOException | NumberFormatException e){
			JOptionPane.showMessageDialog(frame, "Error reading file. Please try again.");
			return false;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("txtfiles/ppe.txt"))) {
			for (String updatedLine : updatedInventory) {
				writer.write(updatedLine);
				writer.newLine();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Error updating inventory.");
			return false;
		}

		return true;
	}

	//Loading table data
	public static void loadTransactionData(DefaultTableModel model) {
		try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/transactions.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				String[] parts = line.split(" / ");
				String transactionID = parts[0].split(": ")[1];
				String itemCode = parts[1].split(": ")[1];
				String quantity = parts[2].split(": ")[1];
				String transactionType = parts[3].split(": ")[1];
                String hsCode = parts[4].split(": ")[1];
                String date = parts[5].split(": ")[1];
                String time = parts[6].split(": ")[1];
				model.addRow(new Object[]{transactionID, itemCode, quantity, transactionType, hsCode, date, time});
			}
		} catch (IOException e) {
			System.out.println("Error");
		}
	}
}
