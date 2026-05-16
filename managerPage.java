import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class managerPage implements ActionListener {

    // gui components
    private JFrame frame = new JFrame();
    private JLabel title = new JLabel("Manager Page");
    private JButton manageUsersButton = new JButton("Manage Users");
    private JButton manageServicesButton = new JButton("Manage Services");
    private JButton manageAppointmentsButton = new JButton("Manage Appointments");
    private JButton viewFeedbackButton = new JButton("View Feedback");
    private JButton viewReportsButton = new JButton("View Reports");
    private JButton logoutButton = new JButton("Logout");

    public managerPage() {
        // gui layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 360);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        title.setBounds(100, 10, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        manageUsersButton.setBounds(100, 50, 200, 30);
        manageServicesButton.setBounds(100, 90, 200, 30);
        manageAppointmentsButton.setBounds(100, 130, 200, 30);
        viewFeedbackButton.setBounds(100, 170, 200, 30);
        viewReportsButton.setBounds(100, 210, 200, 30);
        logoutButton.setBounds(100, 250, 200, 30);
        logoutButton.setBackground(new Color(220, 50, 50));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusable(false);
        manageUsersButton.setBackground(Color.LIGHT_GRAY);
        manageUsersButton.setFocusable(false);
        manageServicesButton.setBackground(Color.LIGHT_GRAY);
        manageServicesButton.setFocusable(false);
        manageAppointmentsButton.setBackground(Color.LIGHT_GRAY);
        manageAppointmentsButton.setFocusable(false);
        viewFeedbackButton.setBackground(Color.LIGHT_GRAY);
        viewFeedbackButton.setFocusable(false);
        viewReportsButton.setBackground(Color.LIGHT_GRAY);
        viewReportsButton.setFocusable(false);

        manageUsersButton.addActionListener(this);
        manageServicesButton.addActionListener(this);
        manageAppointmentsButton.addActionListener(this);
        viewFeedbackButton.addActionListener(this);
        viewReportsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        frame.add(title);
        frame.add(manageUsersButton);
        frame.add(manageServicesButton);
        frame.add(manageAppointmentsButton);
        frame.add(viewFeedbackButton);
        frame.add(viewReportsButton);
        frame.add(logoutButton);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageUsersButton) {
            // goes to account database andmanagement page
            new usersDatabase();
            frame.dispose();
        } else if (e.getSource() == manageServicesButton) {
            // goes to services page
            new servicesPage();
            frame.dispose();
        } else if (e.getSource() == manageAppointmentsButton) {
            // goes to appointments page
            new appointmentsPage();
            frame.dispose();
        } else if (e.getSource() == viewFeedbackButton) {
            // goes to feedback page
            new feedbackViewer();
            frame.dispose();
        } else if (e.getSource() == viewReportsButton) {
            // goes to reports page
            new statisticsReports();
            frame.dispose();
        } else if (e.getSource() == logoutButton) {
            new Main();
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        new managerPage();
    }
}
