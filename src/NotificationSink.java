import java.rmi.RemoteException;


public class NotificationSink implements NotificationSinkRemoteInterface{

	@Override
	public void passNotification(Notification messanger) throws RemoteException {
		System.out.println(messanger.getMessage());
	}

}
