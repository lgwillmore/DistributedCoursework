import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface NotificationSourceRemoteInterface extends Remote{
	
	public boolean registerSubscriber(NotificationSink newSink) throws RemoteException;

}
