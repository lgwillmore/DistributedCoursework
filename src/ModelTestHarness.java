import java.io.File;
import java.rmi.Naming;

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
		NotificationSink SinkClient=null;
		try {
			SinkClient = new NotificationSink();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		// Connect Client to Server
		String address=null;
		if(useLocal)address=Util.getlocalIP();
		else address = Util.getExternalIP();
		SinkClient.connectToHost(address);
		
		//add a source to the source manager
		File file= new File("Test");
		mySourceM.addSource(file);	
		
		String remoteSourcePath = SinkClient.getAvailableSourceList().get(0);
		
		SinkClient.registerWith(remoteSourcePath);
		
	}	

}
