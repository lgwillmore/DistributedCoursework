import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class ModelTestHarness {
	
	private static boolean useLocal=true;

	public static void main(String[] args) {	

		// start RMI
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (java.rmi.server.ExportException e) {
			System.out.println("Registry is already started");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
		}
		// Build Source Server Side
		SourceManager mySourceM=null;
		try {
			mySourceM = new SourceManager();
			Naming.rebind("SourceManager", mySourceM);
			System.out.println("SourceManager ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start Serving
		mySourceM.startServing();		
		
		// Build Sink Client Side
		SinkManager mySinkM = new SinkManager();
		
		// Connect Client to Server
		String address=null;
		if(useLocal)address=Util.getlocalIP();
		else address = Util.getExternalIP();
		mySinkM.connectToSource(address);
		
		//add a source to the source manager
		mySourceM.addSource("Test");	
		
		String remoteSourcePath = mySinkM.getSourcePathList().get(0);
		
		mySinkM.registerWith(remoteSourcePath);
		
	}	

}
