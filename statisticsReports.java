import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class statisticsReports implements ActionListener {

    private JFrame frame = new JFrame();
    private JButton returnButton = new JButton("Return to Main Page");

    public statisticsReports() {

        // gui layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(returnButton);
        returnButton.addActionListener(this);
        returnButton.setBackground(new Color(220, 50, 50));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusable(false);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2));
        chartsPanel.add(createGenderPieChart());
        chartsPanel.add(createServiceBarChart());
        frame.add(chartsPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private ChartPanel createGenderPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        int male = 0, female = 0, unspecified = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 10 && line.contains("Role: Customer")) {
                    String gender = parts[8].split(": ")[1].trim();
                    if (gender.equals("Male")) {
                        male++;
                    } else if (gender.equals("Female")) {
                        female++;
                    } else {
                        unspecified++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataset.setValue("Male", male);
        dataset.setValue("Female", female);
        if (unspecified > 0) {
            dataset.setValue("Unspecified", unspecified);
        }

        JFreeChart chart = ChartFactory.createPieChart("Customer Gender Distribution", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 400));
        return chartPanel;
    }

    private ChartPanel createServiceBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int majorService = 0, normalService = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 9) {
                    String service = parts[2].split(": ")[1].trim();
                    if (service.equals("Major Service")) {
                        majorService++;
                    } else if (service.equals("Normal Service")) {
                        normalService++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataset.addValue(majorService, "Appointments", "Major Service");
        dataset.addValue(normalService, "Appointments", "Normal Service");

        JFreeChart chart = ChartFactory.createBarChart("Service Distribution", "Service Type", "Number of Appointments",
                dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 400));
        return chartPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnButton) {
            frame.dispose();
            new managerPage();
        }
    }

    public static void main(String[] args) {
        new statisticsReports();
    }

}
