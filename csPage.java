import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class csPage implements ActionListener {

    // gui components
    private JFrame frame = new JFrame();
    private JLabel title = new JLabel("Counter Staff Page");
    private JButton editProfileButton = new JButton("Edit Profile");
    private JButton manageAppointmentsButton = new JButton("Manage Appointments");
    private JButton manageCustomersButton = new JButton("Manage Customers");
    private JButton collectPaymentButton = new JButton("Collect Payment");
    private JButton genereateReceiptButton = new JButton("Generate Receipt");
    private JButton logoutButton = new JButton("Logout");

    public csPage() {
        // gui layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 360);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        title.setBounds(120, 10, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        editProfileButton.setBounds(100, 50, 200, 30);
        manageAppointmentsButton.setBounds(100, 90, 200, 30);
        manageCustomersButton.setBounds(100, 130, 200, 30);
        collectPaymentButton.setBounds(100, 170, 200, 30);
        genereateReceiptButton.setBounds(100, 210, 200, 30);
        logoutButton.setBounds(100, 250, 200, 30);
        logoutButton.setBackground(new Color(220, 50, 50));
        logoutButton.setForeground(Color.WHITE);
        editProfileButton.setBackground(Color.LIGHT_GRAY);
        manageAppointmentsButton.setBackground(Color.LIGHT_GRAY);
        manageCustomersButton.setBackground(Color.LIGHT_GRAY);
        collectPaymentButton.setBackground(Color.LIGHT_GRAY);
        genereateReceiptButton.setBackground(Color.LIGHT_GRAY);
        editProfileButton.setFocusable(false);
        manageAppointmentsButton.setFocusable(false);
        manageCustomersButton.setFocusable(false);
        collectPaymentButton.setFocusable(false);
        genereateReceiptButton.setFocusable(false);
        logoutButton.setFocusable(false);

        editProfileButton.addActionListener(this);
        manageAppointmentsButton.addActionListener(this);
        manageCustomersButton.addActionListener(this);
        collectPaymentButton.addActionListener(this);
        genereateReceiptButton.addActionListener(this);
        logoutButton.addActionListener(this);

        frame.add(title);
        frame.add(editProfileButton);
        frame.add(manageAppointmentsButton);
        frame.add(manageCustomersButton);
        frame.add(collectPaymentButton);
        frame.add(genereateReceiptButton);
        frame.add(logoutButton);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editProfileButton) {
            // goes to edit profile page
            new editProfile();
            frame.dispose();
        } else if (e.getSource() == manageAppointmentsButton) {
            // goes to appointments page
            new appointmentsPage();
            frame.dispose();
        } else if (e.getSource() == manageCustomersButton) {
            new usersDatabase();
            frame.dispose();
        } else if (e.getSource() == collectPaymentButton) {
            new paymentsPage();
            frame.dispose();
        } else if (e.getSource() == genereateReceiptButton) {
            new paymentsPage();
            frame.dispose();
        } else if (e.getSource() == logoutButton) {
            new Main();
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        new csPage();
    }
}