import java.awt.Container;
import java.rmi.Naming;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DropNotifyBox extends JFrame {
	SourceManager myServer;
	SinkManager myClient;

	public DropNotifyBox() {
		super();
		myClient= new SinkManager();
	}

	public void init() {
		startServer();
		//Build GUI
		Container top = this.getContentPane();
		
	}

	private void startServer() {
		// Build Server Side
		myServer = null;
		try {
			myServer = new SourceManager();
			Naming.rebind("SourceManager", myServer);
			System.out.println("SourceManager ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start Serving
		myServer.startServing();
	}
	
	private class ServerPane extends JPanel{
		
	}
	
	
	private class ClientPane extends JPanel{
		
	}
}
