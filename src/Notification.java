import java.io.Serializable;
import java.util.ArrayList;

public class Notification implements Serializable{
	private ArrayList<String> messages;
	public Notification(ArrayList<String> messages){
		this.messages=messages;
	}
	public ArrayList<String> getMessage(){
		return messages;
	}
}
