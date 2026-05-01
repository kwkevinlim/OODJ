import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class managerPage implements ActionListener{

    //gui components
    private JFrame frame = new JFrame();
    private JLabel title = new JLabel("Manager Page");
    private JButton manageUsersButton = new JButton("Manage Users");
    private JButton manageServicesButton = new JButton("Manage Services");
    private JButton manageAppointmentsButton = new JButton("Manage Appointments");
    private JButton viewFeedbackButton = new JButton("View Feedback and Comments");
    private JButton viewReportsButton = new JButton("View Reports");
    private JButton logoutButton = new JButton("Logout");

    public managerPage() {
        //gui layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        title.setBounds(100, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        manageUsersButton.setBounds(100, 70, 200, 30);
        manageServicesButton.setBounds(100, 110, 200, 30);
        manageAppointmentsButton.setBounds(100, 150, 200, 30);
        viewFeedbackButton.setBounds(100, 190, 200, 30);
        viewReportsButton.setBounds(100, 230, 200, 30);
        logoutButton.setBounds(100, 270, 200, 30);

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
        if (e.getSource()==manageUsersButton) {
            //goes to account database andmanagement page
            new usersDatabase();
            frame.dispose();
        } else if (e.getSource()==manageServicesButton) {
            //goes to services page
            new services();
            frame.dispose();
        } else if (e.getSource()==manageAppointmentsButton) {
            //goes to appointments page
            new appointments();
            frame.dispose();
        } else if (e.getSource()==viewFeedbackButton) {
            //goes to feedback page
            // new feedback();
            frame.dispose();
        } else if (e.getSource()==viewReportsButton) {
            //goes to reports page
            // new reports();
            frame.dispose();
        } else if (e.getSource()==logoutButton) {
            new Main();
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        new managerPage();
    }
}

