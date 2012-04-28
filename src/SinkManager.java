import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class SinkManager {

	private NotificationSink mySink;
	private SourceManagerRemoteInterface currentHost;
	private String host;
	

	public SinkManager(){		
		try {
			mySink = new NotificationSink();
			Naming.rebind("mySink", mySink);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connectToSource(String host) {
		this.host=host;
		String remoteAddress = Util.RMI_PRE+host+"/SourceManager";
		System.out.println(remoteAddress);
		try {
			currentHost = (SourceManagerRemoteInterface) Naming
					.lookup(remoteAddress);
			System.out.println("SourceManager found");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public  ArrayList<String> getSourcePathList(){
		ArrayList<String> result = null;
		try {			
			result= currentHost.getSourcePathList();
		} catch (RemoteException e) {			
			e.printStackTrace();
		}
		System.out.println("Client found these sources: ");
		for (String string : result) {
			System.out.println(string);
		}
		return result;
	}
	
	public void registerWith(String remoteName){
		try {
			System.out.println("Trying to register with "+remoteName);
			NotificationSourceRemoteInterface source=(NotificationSourceRemoteInterface)Naming
					.lookup(remoteName);
			System.out.println("Source found: "+remoteName);
			source.registerSubscriber(Util.getRemoteNameOf("mySink"));
		} catch (RemoteException e) {			
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String translateToRemoteAddress(String name){
		return Util.RMI_PRE+host+name;
	}

}
