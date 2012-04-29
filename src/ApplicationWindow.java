import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextField;



public class ApplicationWindow extends JFrame implements ActionListener {
	private boolean local = false;
	private static int PORT = 0;
	private SourceManager myServer;
	private NotificationSink myClient;
	private JPanel contentPane;
	private JTextField ipField;
	private JTextField ipConnect;
	private JButton btnConnect, btnClear, btnRemoveSub, btnAddSub,
			btnAddSource, btnRemoveSource;
	private JList subscribedList, availableList, sourceView;
	private DefaultListModel sourceModel,subModel,avModel;
	private JTextArea inboxTextArea;
	private static String B_CONNECT = "connect", B_CLEAR = "clear",
			B_REM_SUB = "brs", B_ADD_SUB = "bas", B_ADD_SOURCE = "basource",
			B_REM_SOURCE = "brsource";
	private IPAddressValidator ipvalidator = new IPAddressValidator();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// start RMI
		if (System.getSecurityManager()==null){
			System.setSecurityManager(new SecurityManager());
		}
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (java.rmi.server.ExportException e) {
			System.out.println("Registry is already started");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
		}
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
		super();
		ipvalidator = new IPAddressValidator();
		try {
			myClient = new NotificationSink();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		startServer();
		myClient.setView(this);
		myServer.setView(this);
		init();
	}

	private void startServer() {
		// Build Server Side
		myServer = null;
		try {
			myServer = new SourceManager();
			SourceManager serverStub=(SourceManager)UnicastRemoteObject.exportObject(myServer,PORT);
			LocateRegistry.getRegistry().rebind("SourceManager", serverStub);
			System.out.println("SourceManager ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start Serving
		myServer.startServing();
	}

	private void init() {
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

		btnClear = new JButton("Clear");
		btnClear.setBounds(110, 657, 100, 27);
		btnClear.setName(ApplicationWindow.B_CLEAR);
		btnClear.addActionListener(this);
		Client.add(btnClear);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(335, 53, 583, 81);
		Client.add(panel);
		panel.setLayout(null);

		btnConnect = new JButton("Connect");
		btnConnect.setBounds(422, 25, 100, 27);
		btnConnect.setName(ApplicationWindow.B_CONNECT);
		btnConnect.addActionListener(this);
		panel.add(btnConnect);

		JLabel lblServerIp = new JLabel("Server IP :");
		lblServerIp.setFont(new Font("Dialog", Font.BOLD, 14));
		lblServerIp.setBounds(23, 31, 107, 15);
		panel.add(lblServerIp);

		ipConnect = new JTextField();
		ipConnect.setBounds(121, 29, 249, 19);
		panel.add(ipConnect);
		ipConnect.setColumns(10);

		JLabel lblNewLabel = new JLabel("Available Notification Sources:");
		lblNewLabel.setBounds(334, 147, 227, 28);
		Client.add(lblNewLabel);
		
		subModel=new DefaultListModel();
		subscribedList = new JList(subModel);
		subscribedList.setBounds(670, 181, 281, 449);
		Client.add(subscribedList);

		JLabel lblSubscribedNotificationSources = new JLabel(
				"Subscribed Notification Sources:");
		lblSubscribedNotificationSources.setBounds(670, 141, 248, 34);
		Client.add(lblSubscribedNotificationSources);

		btnRemoveSub = new JButton("<");
		btnRemoveSub.setBounds(670, 658, 117, 25);
		btnRemoveSub.setName(ApplicationWindow.B_REM_SUB);
		btnRemoveSub.addActionListener(this);
		Client.add(btnRemoveSub);

		btnAddSub = new JButton(">");
		btnAddSub.setBounds(512, 658, 117, 25);
		btnAddSub.setName(ApplicationWindow.B_ADD_SUB);
		btnAddSub.addActionListener(this);
		Client.add(btnAddSub);

		avModel=new DefaultListModel();
		availableList = new JList(avModel);
		availableList.setBounds(334, 187, 291, 443);
		Client.add(availableList);

		inboxTextArea = new JTextArea();
		inboxTextArea.setVerifyInputWhenFocusTarget(false);
		inboxTextArea.setBounds(20, 69, 281, 558);
		Client.add(inboxTextArea);

		JLabel lblInbox = new JLabel("Inbox");
		lblInbox.setFont(new Font("Dialog", Font.BOLD, 14));
		lblInbox.setBounds(20, 37, 100, 20);
		Client.add(lblInbox);

		JPanel Server = new JPanel();
		tabbedPane.addTab("Server", null, Server, null);
		Server.setLayout(null);

		sourceModel=new DefaultListModel();
		sourceView = new JList(sourceModel);
		sourceView.setSize(new Dimension(800, 600));
		sourceView.setBounds(174, 108, 668, 509);				
		Server.add(sourceView);

		btnAddSource = new JButton("Add Source");
		btnAddSource.setBounds(326, 654, 169, 27);
		btnAddSource.setName(ApplicationWindow.B_ADD_SOURCE);
		btnAddSource.addActionListener(this);
		Server.add(btnAddSource);

		btnRemoveSource = new JButton("Remove Source");
		btnRemoveSource.setBounds(590, 654, 153, 27);
		btnRemoveSource.setName(ApplicationWindow.B_REM_SOURCE);
		btnRemoveSource.addActionListener(this);
		Server.add(btnRemoveSource);

		JLabel lblNotificationSourcesYour = new JLabel(
				"Notification Sources your Server has made visible");
		lblNotificationSourcesYour.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNotificationSourcesYour.setBounds(176, 80, 486, 27);
		Server.add(lblNotificationSourcesYour);

		JLabel lblYourServersIp = new JLabel("Your Server's IP address:");
		lblYourServersIp.setFont(new Font("Dialog", Font.BOLD, 14));
		lblYourServersIp.setBounds(174, 31, 213, 15);
		Server.add(lblYourServersIp);

		ipField = new JTextField();
		ipField.setEditable(false);
		ipField.setBounds(405, 29, 237, 19);
		ipField.setText(getIP());
		Server.add(ipField);
		ipField.setColumns(10);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		String name = b.getName();
		if (name.equals(ApplicationWindow.B_ADD_SOURCE)) {
			displayFileChooser();
		} else if (name.equals(ApplicationWindow.B_REM_SOURCE)) {
			removeSource();
		} else if (name.equals(ApplicationWindow.B_ADD_SUB)) {
			addSubscription();
		} else if (name.equals(ApplicationWindow.B_REM_SUB)) {
			removeSubscription();
		} else if (name.equals(ApplicationWindow.B_CONNECT)) {
			connect();
		} else if (name.equals(ApplicationWindow.B_CLEAR)) {
			inboxTextArea.setText("");
			inboxTextArea.repaint();
		}
	}

	private void removeSubscription() {
		String remoteName;
		if(!subscribedList.isSelectionEmpty()){
			remoteName=subscribedList.getSelectedValue().toString();
			myClient.deregisterWith(remoteName);
			populateSubscriptions();
			inboxTextArea.append("REMOVED SUBSCRIPTION to: "+remoteName+"\n");
			
		}	
	}

	private void addSubscription() {
		String remoteName;
		if(!availableList.isSelectionEmpty()){
			remoteName=availableList.getSelectedValue().toString();
			if(!myClient.getSubscriptionList().contains(remoteName)){
				myClient.registerWith(remoteName);
				populateSubscriptions();
				inboxTextArea.append("SUBSCRIBED to: "+remoteName+"\n");
			}
		}		
	}

	private void connect() {
		String ip = this.ipConnect.getText();
		boolean valid=ipvalidator.validate(ip);
		if(valid){
			myClient.connectToHost(ip);			
			inboxTextArea.append("Connected to Host at: "+ip+"\n");
			inboxTextArea.repaint();
			populateAvailableSources();
		}
		else{
			inboxTextArea.append("Invalid IP address\n");
			inboxTextArea.repaint();
			ipConnect.setText("");
			ipConnect.repaint();	
		}		
	}

	private String getIP() {
		if (local)
			return Util.getlocalIP();
		return Util.getExternalIP();
	}
	
	private void populateVisibleSources(){
		ArrayList<String> sources = myServer.getSourcePathListLocal();
		sourceModel.removeAllElements();
		for (int i=0;i<sources.size();i++) {
			sourceModel.add(i,sources.get(i));
		}
	}
	
	private void populateAvailableSources(){		
		ArrayList<String> remoteSources=myClient.getAvailableSourceList();
		avModel.removeAllElements();
		for (int i=0;i<remoteSources.size();i++) {
			avModel.add(i, remoteSources.get(i));
		}
	}
	
	public void populateSubscriptions(){
		ArrayList<String> subSources=myClient.getSubscriptionList();
		subModel.removeAllElements();
		for (int i=0;i<subSources.size();i++) {
			subModel.add(i, subSources.get(i));
		}
	}
	
	private void displayFileChooser(){
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnVal = fc.showOpenDialog(this);
		String path=null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {				
			path = fc.getSelectedFile().toString();		
		}
		if(path!=null){
			File file = new File(path);
			myServer.addSource(file);
			populateVisibleSources();
		}
	}
	
	private void removeSource(){
		int selected=sourceView.getSelectedIndex();
		if(selected>-1){
			myServer.removeSource(sourceModel.get(selected).toString());
			sourceModel.remove(selected);
		}	
	}
	
	public void postNotification(String message){
		inboxTextArea.append("NOTIFICATION : "+message+"\n"); 
	}
	
	public void postMessage(String message){
		inboxTextArea.append(message+"\n");
	}
}
