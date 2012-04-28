import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class NotificationSource extends UnicastRemoteObject implements
		NotificationSourceRemoteInterface {
	FileMonitor myFM;
	String path;
	ArrayList<NotificationSinkRemoteInterface> subscribers;

	protected NotificationSource(String path) throws RemoteException {
		this.path = path;
		subscribers = new ArrayList<NotificationSinkRemoteInterface>();
		try {
			myFM = new FileMonitor(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void notifyRemoval() {
		// TODO Auto-generated method stub
	}

	public void doChecks() {
		ArrayList<String> messages = myFM.check();
		if (messages != null) {
			for (NotificationSinkRemoteInterface sink : subscribers) {
				try {
					System.out.println("Passing notification");
					sink.passNotification(new Notification(messages));
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
			if (!subscribers.contains(newSink))
				subscribers.add(newSink);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
