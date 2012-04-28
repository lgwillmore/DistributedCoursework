import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface SourceManagerRemoteInterface extends Remote{
	
	public ArrayList<NotificationSource> getSourceList() throws RemoteException;

}
