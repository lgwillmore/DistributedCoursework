import java.io.File;
import java.net.InetAddress;
import java.rmi.Naming;

public class SinkManager {

	private NotificationSink mySink;
	private SourceManagerRemoteInterface currentHost;

	public SinkManager() {
		mySink = new NotificationSink();
	}

	public void connectToSource(String host) {
		String remoteAddress = "rmi://"+host+"/SourceManager";
		System.out.println(remoteAddress);
		try {
			SourceManagerRemoteInterface remoteThing = (SourceManagerRemoteInterface) Naming
					.lookup(remoteAddress);
			System.out.println("SourceManager found");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
