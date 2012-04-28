import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class NotificationSink extends UnicastRemoteObject implements NotificationSinkRemoteInterface{

	protected NotificationSink() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void passNotification(Notification messanger) throws RemoteException {
		System.out.println(messanger.getMessage());
	}

}
