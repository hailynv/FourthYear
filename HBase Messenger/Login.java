import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Login {

	private JFrame frmLogin;
	private JTextField userField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 325, 156);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);

		JLabel lblUsername = new JLabel("Username: ");
		lblUsername.setBounds(6, 38, 81, 15);
		frmLogin.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(6, 66, 75, 15);
		frmLogin.getContentPane().add(lblPassword);

		JLabel lblNewLabel = new JLabel("Welcome");
		lblNewLabel.setBounds(116, 6, 64, 15);
		frmLogin.getContentPane().add(lblNewLabel);

		userField = new JTextField();
		userField.setBounds(88, 36, 200, 19);
		frmLogin.getContentPane().add(userField);
		userField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setToolTipText("The password is password");
		passwordField.setBounds(88, 65, 200, 19);
		frmLogin.getContentPane().add(passwordField);

		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String username, password;
				password = passwordField.getText();
				username = userField.getText();

				if(username.length() == 0 || !username.matches("^(.+)@(.+)$"))
				{
					JOptionPane.showMessageDialog(null, "Enter a valid username.");
				}
				else if (!password.contentEquals("password") || password.length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Invalid password.");
				}
				else
				{
					 
					try {
						GenerateMessages.main(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					frmLogin.setVisible(false);
					Messages.main(username);

				}

			}
		});
		btnNewButton.setBounds(131, 97, 73, 25);
		frmLogin.getContentPane().add(btnNewButton);
	}
}