import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface NotificationSinkRemoteInterface extends Remote{
	public void passNotification(Notification message) throws RemoteException;
}
