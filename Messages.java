import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JEditorPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

import javax.swing.JList;
import javax.swing.JTextArea;

public class Messages {

    private LocalDateTime date = LocalDateTime.now();
	private JFrame frmMessages;
	private final JLabel lblDateTime = new JLabel(date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + " " + date.getYear());
	private JTextField textField;
	private JTextField textField_1;
	private static String senderEmail, receiverEmail, messageTitle, messageBody;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private JList<Message> list = new JList<Message>();
	private JTextArea textArea = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String username) {
		senderEmail = username;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Messages window = new Messages();
					window.frmMessages.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Messages() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() {
		frmMessages = new JFrame();
		frmMessages.setTitle("Messages");
		frmMessages.setBounds(100, 100, 650, 450);
		frmMessages.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMessages.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		panel.setBounds(6, 6, 325, 416);
		frmMessages.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblWelcome = new JLabel("Welcome, " + senderEmail);
		lblWelcome.setBounds(6, 6, 307, 15);
		panel.add(lblWelcome);
		lblDateTime.setBounds(6, 25, 307, 15);
		panel.add(lblDateTime);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMessages.setVisible(false);
				Login.main(null);
			}
		});
		btnLogout.setBounds(229, 20, 84, 25);
		panel.add(btnLogout);
		
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(6, 79, 22, 15);
		panel.add(lblTo);
		
		JLabel lblFrom = new JLabel("From: " + senderEmail);
		lblFrom.setBounds(6, 55, 307, 15);
		panel.add(lblFrom);
		
		textField = new JTextField();
		textField.setToolTipText("User to recieve your message");
		textField.setBounds(37, 77, 276, 19);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setBounds(6, 105, 58, 15);
		panel.add(lblSubject);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("Message subject");
		textField_1.setBounds(73, 103, 240, 19);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setToolTipText("Enter the body of your message here.");
		editorPane.setBounds(16, 133, 297, 247);
		panel.add(editorPane);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				receiverEmail = textField.getText();
				messageTitle = textField_1.getText();
				messageBody = editorPane.getText();
				
				if (receiverEmail.length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Empty message recipient.");
				}
				else if (messageTitle.length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Empty message title.");
				}
				else if(messageBody.length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Empty message body.");
				}
				else
				{
					try {
						SendMessages.main(senderEmail, receiverEmail, messageTitle, messageBody);
						textField.setText("");
						textField_1.setText("");
						editorPane.setText("");
						JOptionPane.showMessageDialog(null, "Message sent.");
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Message not sent.");
						e1.printStackTrace();
					}
				}
				
			}
		});
		btnSend.setBounds(119, 385, 70, 25);
		panel.add(btnSend);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		panel_1.setBounds(338, 6, 306, 416);
		frmMessages.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(20, 194, 266, 179);
		textArea.setLineWrap(true);
		panel_1.add(textArea);
		
		DefaultListModel<Message> dlm = new DefaultListModel<Message>();
		list.setBounds(19, 27, 266, 155);
		try 
		{
			messages = ListMessages.main(senderEmail);
			for(Message message : messages)
			{
				dlm.addElement(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		list.setModel(dlm);
		
		
		JLabel lblInbox = new JLabel("Inbox");
		lblInbox.setBounds(130, 6, 61, 16);
		panel_1.add(lblInbox);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
		scrollPane.setBounds(20, 57, 266, 121);
		panel_1.add(scrollPane);
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlm.removeAllElements();
				try 
				{
					messages = ListMessages.main(senderEmail);
					for(Message message : messages)
					{
						dlm.addElement(message);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				list.setModel(dlm);
			}
		});
		btnNewButton.setBounds(104, 385, 89, 25);
		panel_1.add(btnNewButton);
		btnNewButton.setToolTipText("Check for new messages");
		
		
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				Message selectedMessage = list.getSelectedValue();
				
				textArea.setText(selectedMessage.mBody);
				
			}
			
		});
		
		
		
	}

}