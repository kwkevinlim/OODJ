import javax.swing.*;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;


public class AccountSignUp implements ActionListener {
    
    private JFrame frame = new JFrame();
    private Choice choice = new Choice();
    private JButton rolebutton = new JButton("Confirm role");
    private JLabel title = new JLabel("Select Role");

    private String username;
    private String password;

    public AccountSignUp(String username, String password) {

        this.username = username;
        this.password = password;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,250);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        title.setBounds(10,25,100,30);
        title.setFont(new Font(null, Font.BOLD,16));
        choice.setBounds(100,75,80,40);
        rolebutton.setBounds(130, 150,120,30);
        rolebutton.setFocusable(false);
        rolebutton.setBackground(Color.LIGHT_GRAY);

        rolebutton.addActionListener(this);

        choice.add("Manager");
        choice.add("Technician");
        choice.add("Counter Staff");
        choice.add("Customer");

        frame.add(choice);
        frame.add(rolebutton);
        frame.add(title);

    }


    @Override
    public void actionPerformed(ActionEvent onclick) {
        String selectedRole = choice.getSelectedItem();
        if (onclick.getSource()==rolebutton) {
            UserUtils.saveUser(username, password, selectedRole, frame);
        }
        frame.dispose();
    }
}
