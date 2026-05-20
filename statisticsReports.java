import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.util.Map;
import java.util.HashMap;

//maybe I could add seubsections, like general system statistics, customer statistics, business data analytics
public class statisticsReports implements ActionListener {

    private JFrame frame = new JFrame();
    private JButton returnButton = new JButton("Return to Main Page");

    public statisticsReports() {

        // gui layout
        // to figure out how to get auto height
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(returnButton);
        returnButton.addActionListener(this);
        returnButton.setBackground(new Color(220, 50, 50));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusable(false);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(createSection("General System Statistics", new ChartPanel[] {
                createUserBreakdownPieChart()
        }));
        mainPanel.add(createSection("Customer Statistics", new ChartPanel[] {
                createGenderPieChart(),
                carBrandBreakdownPieChart()
        }));
        mainPanel.add(createSection("Business Data Analytics", new ChartPanel[] {
                createServiceBarChart(),
                serviceRevenueBarChart()
        }));
        mainPanel.add(createSection("", new ChartPanel[] {
                appointmentStatusBreakdownBarChart()
        }));

        frame.add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createSection(String title, ChartPanel[] charts) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(separator);
        section.add(Box.createVerticalStrut(5));

        JPanel chartRow = new JPanel(new GridLayout(1, charts.length));
        for (ChartPanel chart : charts) {
            chartRow.add(chart);
        }
        chartRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        int chartHeight = 400;
        int chartWidth = 950;
        chartRow.setPreferredSize(new Dimension(chartWidth, chartHeight));
        chartRow.setMaximumSize(new Dimension(chartWidth, chartHeight));
        section.add(chartRow);

        return section;
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
        chart.getLegend().setVisible(false);
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    private ChartPanel createUserBreakdownPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        int customer = 0, manager = 0, cs = 0, technician = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 10) {
                    String role = parts[3].split(": ")[1].trim();
                    if (role.equals("Customer")) {
                        customer++;
                    } else if (role.equals("Manager")) {
                        manager++;
                    } else if (role.equals("Counter Staff")) {
                        cs++;
                    } else if (role.equals("Technician")) {
                        technician++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataset.setValue("Customer", customer);
        dataset.setValue("Manager", manager);
        dataset.setValue("Counter Staff", cs);
        dataset.setValue("Technician", technician);

        JFreeChart chart = ChartFactory.createPieChart("Overall User Breakdown", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    private ChartPanel serviceRevenueBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> servicePrice = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/services.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                service s = serviceUtilities.convertToObject(line);
                try {
                    servicePrice.put(s.getServiceName(), Double.parseDouble(s.getPrice()));
                } catch (NumberFormatException e) {
                    System.out.println("Error reading price for " + s.getServiceName());
                }

            }
        } catch (Exception e) {
            System.out.println("Error reading services.");
        }

        Map<String, Integer> serviceCount = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(" \\| ");
                if (parts.length == 9) {
                    String serviceName = parts[2].split(": ")[1].trim();
                    serviceCount.put(serviceName, serviceCount.getOrDefault(serviceName, 0) + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading number of services.");
        }

        for (String serviceName : serviceCount.keySet()) {
            if (servicePrice.containsKey(serviceName)) {
                double price = servicePrice.getOrDefault(serviceName, 0.0);
                double revenue = serviceCount.get(serviceName) * price;
                 dataset.addValue(revenue, "Revenue (RM)", serviceName);
            }
        }

        JFreeChart chart = ChartFactory.createBarChart("Service Revenue", "Service", "Revenue (RM)", dataset);
        chart.getLegend().setVisible(false);
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    // dynamically add values using hashmap to obtain them from text file
    private ChartPanel carBrandBreakdownPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> carCount = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 9) {
                    String car = parts[4].split(": ")[1].trim().split(" ")[0];
                    carCount.put(car, carCount.getOrDefault(car, 0) + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String car : carCount.keySet()) {
            dataset.setValue(car, carCount.get(car));
        }

        JFreeChart chart = ChartFactory.createPieChart("Car Brand Breakdown", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    private ChartPanel appointmentStatusBreakdownBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int completed = 0, inProgress = 0, pending = 0, cancelled = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("txtfiles/appointments.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length == 9) {
                    String status = parts[8].split(": ")[1].trim();
                    if (status.equals("Completed")) {
                        completed++;
                    } else if (status.equals("In Progress")) {
                        inProgress++;
                    } else if (status.equals("Pending")) {
                        pending++;
                    } else if (status.equals("Cancelled")) {
                        cancelled++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataset.addValue(completed, "Appointments", "Completed");
        dataset.addValue(inProgress, "Appointments", "In Progress");
        dataset.addValue(pending, "Appointments", "Pending");
        dataset.addValue(cancelled, "Appointments", "Cancelled");
        JFreeChart chart = ChartFactory.createBarChart("Appointment Status Breakdown", "Status",
                "Number of Appointments",
                dataset);
        chart.getLegend().setVisible(false);
        ChartPanel chartPanel = new ChartPanel(chart);
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
