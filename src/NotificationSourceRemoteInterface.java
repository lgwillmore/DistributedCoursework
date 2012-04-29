import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface NotificationSourceRemoteInterface extends Remote{
	
	public void registerSubscriber(String remoteName) throws RemoteException;

	public void deregisterSubscriber(String remoteName) throws RemoteException;

}
