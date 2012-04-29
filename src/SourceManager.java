import java.io.File;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class SourceManager extends UnicastRemoteObject implements SourceManagerRemoteInterface{
	
	public static final String REGISTRY_NAME = "SourceManager"; 
	private HashMap<String,NotificationSource> activeSources;
	private Thread serveThread;
	private ApplicationWindow view;
	
	protected SourceManager() throws RemoteException {
		super();
		activeSources=new HashMap<String,NotificationSource>();
	}
	
	public void startServing(){
		System.out.println("SourceManager started");
		serveThread=new Thread(new SourceServeEngine());
		serveThread.start();
	}
	
	
	public void addSource(File file){
		String path=null;
		try {
			path = file.getCanonicalPath();
		} catch (IOException e1) {			
			e1.printStackTrace();
		}
		String url = Util.windowsPathToURL(path);
		System.out.println(url);
		if(!activeSources.containsKey(path)){
			System.out.println(path);
			try {
				NotificationSource ns = new NotificationSource(path,Util.getRemoteNameOf(url));
				Naming.rebind(Util.getRemoteNameOf(url), ns);
				activeSources.put(url, ns);
				System.out.println("Source manager added Source to:"+path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
		}		
	}
	
	public void removeSource(String path){
		if(activeSources.containsKey(path)){
			activeSources.get(path).notifyRemoval();
			activeSources.remove(path);
		}
	}


	@Override
	public ArrayList<String> getSourcePathList() throws RemoteException {
		ArrayList<String> list = new ArrayList<String>();
		for (String path : activeSources.keySet()) {
			list.add(Util.getRemoteNameOf(path));		
		}
		return list;
	}
	
	@Override
	public NotificationSource getSource(String path)throws RemoteException{
		return activeSources.get(path);
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

	public ArrayList<String> getSourcePathListLocal() {
		ArrayList<String> list = new ArrayList<String>();
		for (String path : activeSources.keySet()) {
			list.add(path);		
		}
		return list;
	}

	public void setView(ApplicationWindow view) {
		this.view=view;
	}

}
