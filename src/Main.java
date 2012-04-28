import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;

public class Main {

	public static void main(String[] args) {	

		// start RMI
		try {
			java.rmi.registry.LocateRegistry.createRegistry(80);
			System.out.println("RMI registry ready.");
		} catch (java.rmi.server.ExportException e) {
			System.out.println("Registry is already started");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
		}
		// Build Source Server Side
		SourceManager mySourceM;
		try {
			mySourceM = new SourceManager();
			Naming.rebind("SourceManager", mySourceM);
			System.out.println("SourceManager ready");
		} catch (Exception e) {
		}

		// Build Sink Client Side
		SinkManager mySinkM = new SinkManager();
		// Connect Client to Server					
		mySinkM.connectToSource(getExternalIP());
	}
	
	private static String getExternalIP(){
		URL whatismyip;
		try {
			whatismyip = new URL(
					"http://automation.whatismyip.com/n09230945.asp");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));

			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
