import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationSink extends UnicastRemoteObject implements NotificationSinkRemoteInterface{

	private HashMap<String,NotificationSourceRemoteInterface> subscriptions;
	private SourceManagerRemoteInterface currentHost;
	private String host;
	private ApplicationWindow view;
	public static String REGISTRY_NAME="NotificationSink";
	Registry ServerRegistry;
	

	protected  NotificationSink()throws RemoteException{
		super();
		subscriptions=new HashMap<String, NotificationSourceRemoteInterface>();
		try{
			NotificationSink sinkStub = (NotificationSink)UnicastRemoteObject.exportObject(this,ApplicationWindow.PORT);
			LocateRegistry.getRegistry().rebind(NotificationSink.REGISTRY_NAME, sinkStub);			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connectToHost(String host) {
		this.host=host;
		try {
			ServerRegistry = LocateRegistry.getRegistry(host);
		} catch (RemoteException e1) {			
			e1.printStackTrace();
		}
		//String remoteAddress = Util.RMI_PRE+host+"/"+SourceManager.REGISTRY_NAME;
		//System.out.println(remoteAddress);
		try {
			currentHost = (SourceManagerRemoteInterface)ServerRegistry.lookup(SourceManager.REGISTRY_NAME);
			System.out.println("SourceManager found");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public  ArrayList<String> getAvailableSourceList(){
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
			source.registerSubscriber(Util.getRemoteNameOf(NotificationSink.REGISTRY_NAME));
			subscriptions.put(remoteName, source);
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
	
	public void deregisterWith(String remoteAddress){
		if(subscriptions.containsKey(remoteAddress)){
			try {
				subscriptions.get(remoteAddress).deregisterSubscriber(Util.getRemoteNameOf(NotificationSink.REGISTRY_NAME));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			subscriptions.remove(remoteAddress);
		}
	}
	
	public String translateToRemoteAddress(String name){
		return Util.RMI_PRE+host+name;
	}

	public void setView(ApplicationWindow view) {
		this.view=view;
	}
	
	@Override
	public void passNotification(Notification messanger) throws RemoteException {
		for (String message : messanger.getMessage()) {
			view.postNotification(message);
		}
	}

	public ArrayList<String> getSubscriptionList() {
		ArrayList<String> result=new ArrayList<String>();
		for (String string : subscriptions.keySet()) {
			result.add(string);
		}
		return result;
	}

	@Override
	public void notifySourceCancelled(String registryName)
			throws RemoteException {
		if(subscriptions.containsKey(registryName)){
			subscriptions.remove(registryName);
			view.populateSubscriptions();
			view.postMessage("SOURCE CANCELLED BY HOST: "+registryName+"\n(Reccommend Connection Refresh)");
		}
	}

}
