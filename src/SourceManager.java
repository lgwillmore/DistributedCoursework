import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class SourceManager extends UnicastRemoteObject implements SourceManagerRemoteInterface{
	
	private HashMap<String,NotificationSource> activeSources;
	private Thread serveThread;
	
	protected SourceManager() throws RemoteException {
		super();
		activeSources=new HashMap<String,NotificationSource>();
	}
	
	public void startServing(){
		serveThread=new Thread(new SourceServeEngine());
		serveThread.start();
	}
	
	
	public void addSource(String path){
		if(!activeSources.containsKey(path))activeSources.put(path, new NotificationSource(path));
	}
	
	public void removeSource(String path){
		if(!activeSources.containsKey(path)){
			activeSources.get(path).notifyRemoval();
			activeSources.remove(path);
		}
	}


	@Override
	public ArrayList<NotificationSource> getSourceList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class SourceServeEngine implements Runnable{
		boolean on=true;

		@Override
		public void run() {
			while(on){
				waitABit();
				checkSources();
			}
		}

		private void checkSources() {
			while(activeSources.size()>0){
				waitABit();
				for (NotificationSource source : activeSources.values()) {
					source.doChecks();
				}
			}
		}
		
		private void waitABit(){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {					
				e.printStackTrace();
			}
		}
		
	}

}
