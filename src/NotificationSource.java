import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationSource extends UnicastRemoteObject implements
		NotificationSourceRemoteInterface {
	private FileMonitor myFM;
	private String path;
	private String Registry_Name;
	private HashMap<String,NotificationSinkRemoteInterface> subscribers;

	protected NotificationSource(String path, String remoteName) throws RemoteException {
		this.Registry_Name = remoteName;
		this.path=path;
		subscribers = new HashMap<String,NotificationSinkRemoteInterface>();
		try {
			myFM = new FileMonitor(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void notifyRemoval() {
		for (String sink : subscribers.keySet()) {
			try {
				System.out.println("notifying of cancellation");
				subscribers.get(sink).notifySourceCancelled(Registry_Name); 
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void doChecks() {
		ArrayList<String> messages = myFM.check();
		if (messages != null) {
			for (String sink : subscribers.keySet()) {
				try {
					System.out.println("Passing notification");
					subscribers.get(sink).passNotification(new Notification(messages));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void registerSubscriber(String remoteName) throws RemoteException {
		try {
			NotificationSinkRemoteInterface newSink = (NotificationSinkRemoteInterface) Naming
					.lookup(remoteName);
			if (!subscribers.containsKey(remoteName))
				subscribers.put(remoteName, newSink);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deregisterSubscriber(String remoteName) throws RemoteException {
		if(subscribers.containsKey(remoteName))subscribers.remove(remoteName);
	}

}
