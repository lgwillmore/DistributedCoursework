import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	public static String RMI_PRE = "rmi://";

	public static String getExternalIP() {
		URL whatismyip;
		try {
			whatismyip = new URL(
					"http://automation.whatismyip.com/n09230945.asp");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));

			String ip = in.readLine(); // you get the IP as a String
			return ip;
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getlocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getRemoteNameOf(String name) {
		return RMI_PRE + getlocalIP() + "/" + name;
	}
	
	public static String windowsPathToURL(String winPath){
		String r1=winPath.trim().toLowerCase();
		String[] r2 =r1.split(":");
		String r3=r2[1].replace('\\', '/');
		return r3;		
	}

}
