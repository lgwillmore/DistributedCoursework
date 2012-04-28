import java.rmi.RemoteException;
import java.util.ArrayList;


public class NotificationSource implements NotificationSourceRemoteInterface{
	
	String path;
	ArrayList<NotificationSink> subscribers;

	public NotificationSource(String path) {
		this.path=path;
	}	

	public void notifyRemoval() {
		// TODO Auto-generated method stub
		
	}
	
	public void doChecks(){
		for (NotificationSink sink : subscribers) {
			try {
				sink.passNotification(new Notification("Hello"));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public boolean registerSubscriber(NotificationSink newSink)
			throws RemoteException {
		if(subscribers.add(newSink))return true;
		return false;
	}

}
