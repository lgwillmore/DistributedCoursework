import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface SourceManagerRemoteInterface extends Remote{
	
	public ArrayList<String> getSourcePathList() throws RemoteException;
	public NotificationSource getSource(String path)throws RemoteException;

}
