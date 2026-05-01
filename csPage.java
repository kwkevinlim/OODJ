import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class csPage implements ActionListener{

    //gui components
    private JFrame frame = new JFrame();
    private JLabel title = new JLabel("Counter Staff Page");
    private JButton editProfileButton = new JButton("Edit Profile");
    private JButton manageAppointmentsButton = new JButton("Manage Appointments");
    private JButton manageCustomersButton = new JButton("Manage Customers");
    private JButton collectPaymentButton = new JButton("Collect Payment");
    private JButton genereateReceiptButton = new JButton("Generate Receipt");
    private JButton logoutButton = new JButton("Logout");

    public csPage() {
        //gui layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        title.setBounds(100, 20, 200, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        editProfileButton.setBounds(100, 70, 200, 30);
        manageAppointmentsButton.setBounds(100, 110, 200, 30);
        manageCustomersButton.setBounds(100, 150, 200, 30);
        collectPaymentButton.setBounds(100, 230, 200, 30);
        genereateReceiptButton.setBounds(100, 270, 200, 30);
        logoutButton.setBounds(100, 310, 200, 30);

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
        if (e.getSource()==editProfileButton) {
            //goes to edit profile page
            new editProfile();
            frame.dispose();
        } else if (e.getSource()==manageAppointmentsButton) {
            //goes to appointments page
            new appointments();
            frame.dispose();
        } else if (e.getSource()==manageCustomersButton) {
            new usersDatabase();
            frame.dispose();
        } else if (e.getSource()==collectPaymentButton) {
            //code to collect payment
            frame.dispose();
        } else if (e.getSource()==genereateReceiptButton) {
            //code to generate receipt
            frame.dispose();
        } else if (e.getSource()==logoutButton) {
            new Main();
            frame.dispose();
        }
    }

    public static void main(String[] args) {
        new csPage();
    }
}