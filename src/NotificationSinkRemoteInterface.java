import java.rmi.Remote;
import java.rmi.RemoteException;


public interface NotificationSinkRemoteInterface extends Remote{
	public void passNotification(Notification message) throws RemoteException;

	public void notifySourceCancelled(String registryName) throws RemoteException;
}
