import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.TextArea;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.List;
import javax.swing.JTextArea;


public class ApplicationWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationWindow frame = new ApplicationWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ApplicationWindow() {
		setMinimumSize(new Dimension(1000, 800));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setMinimumSize(new Dimension(1000, 800));
		contentPane.setBounds(new Rectangle(1000, 800, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
		
		JPanel Client = new JPanel();
		tabbedPane.addTab("Client\n", null, Client, null);
		Client.setLayout(null);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(110, 657, 100, 27);
		Client.add(btnClear);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(334, 42, 583, 81);
		Client.add(panel);
		panel.setLayout(null);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(445, 10, 100, 27);
		panel.add(btnConnect);
		
		JLabel lblNewLabel = new JLabel("Available Notification Sources:");
		lblNewLabel.setBounds(334, 147, 227, 28);
		Client.add(lblNewLabel);
		
		JList subscribedList = new JList();
		subscribedList.setBounds(670, 181, 281, 449);
		Client.add(subscribedList);
		
		JLabel lblSubscribedNotificationSources = new JLabel("Subscribed Notification Sources:");
		lblSubscribedNotificationSources.setBounds(670, 141, 248, 34);
		Client.add(lblSubscribedNotificationSources);
		
		JButton button = new JButton("<");
		button.setBounds(670, 658, 117, 25);
		Client.add(button);
		
		JButton btnNewButton = new JButton(">");
		btnNewButton.setBounds(512, 658, 117, 25);
		Client.add(btnNewButton);
		
		JList list = new JList();
		list.setBounds(334, 187, 291, 443);
		Client.add(list);
		
		JTextArea textArea = new JTextArea();
		textArea.setVerifyInputWhenFocusTarget(false);
		textArea.setBounds(20, 69, 281, 558);
		Client.add(textArea);
		
		JLabel lblInbox = new JLabel("Inbox");
		lblInbox.setFont(new Font("Dialog", Font.BOLD, 14));
		lblInbox.setBounds(20, 37, 100, 20);
		Client.add(lblInbox);
		
		JPanel Server = new JPanel();
		tabbedPane.addTab("Server", null, Server, null);
		Server.setLayout(null);
		
		JList SourceView = new JList();
		SourceView.setSize(new Dimension(800, 600));
		SourceView.setBounds(174, 108, 668, 509);
		Server.add(SourceView);
		
		JButton btnAddSource = new JButton("Add Source");
		btnAddSource.setBounds(326, 629, 169, 27);
		Server.add(btnAddSource);
		
		JButton btnRemoveSource = new JButton("Remove Source");
		btnRemoveSource.setBounds(591, 629, 153, 27);
		Server.add(btnRemoveSource);
		
		JLabel lblNotificationSourcesYour = new JLabel("Notification Sources your Server has made visible");
		lblNotificationSourcesYour.setBounds(174, 53, 486, 27);
		Server.add(lblNotificationSourcesYour);
	}
}
