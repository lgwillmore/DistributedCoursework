import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class will do a single check on a given path when check() is called. It
 * will check for modify,delete if given path is a file. it will check all files
 * for modify,delete,create if given path is a directory.
 * 
 * @author laurence
 * 
 */
public class FileMonitor {
	private File root;
	private Long rootTimeStamp;
	HashMap<File, Long> fileRecord;

	public FileMonitor(String path) throws IOException {
		root = new File(path);
		if (!root.exists())
			throw new IOException("Does not exist : " + path);
		if (root.isDirectory()) {
			fileRecord = new HashMap<File, Long>();
			File[] files = root.listFiles();
			for (File file : files) {
				fileRecord.put(file, file.lastModified());
			}
		}
		rootTimeStamp = root.lastModified();
	}

	/*
	 * Returns an array of messages about the changes that have occurred since
	 * last check
	 */
	public ArrayList<String> check() {
		ArrayList<String> result = new ArrayList<String>();
		if (root.exists()) {
			if (root.isFile()) {
				if (root.lastModified() != rootTimeStamp) {
					rootTimeStamp = root.lastModified();
					result.add("MODIFIED : " + root.getPath());
				}
			} else {
				result = checkDirectory(root);
			}
		} else
			result.add("DELETED : " + root.getPath());
		if (result.size() > 0)
			return result;
		else
			return null;
	}

	/*
	 * Currently not recursive
	 */
	private ArrayList<String> checkDirectory(File directory) {
		ArrayList<String> result = new ArrayList<String>();
		File[] files = directory.listFiles();
		for (File file : files) {
			String r1 = checkFile(file);
			if (r1 != null)
				result.add(r1);
		}
		ArrayList<File> deleteList = new ArrayList<File>();
		for (File file : fileRecord.keySet()) {
			if (!file.exists()) {
				result.add("DELETED : " + file.getPath());
				deleteList.add(file);
			}
		}
		for (File file : deleteList) {
			fileRecord.remove(file);
		}
		return result;
	}

	private String checkFile(File f) {
		if (fileRecord.keySet().contains(f)) {
			if (f.lastModified() != fileRecord.get(f)) {
				fileRecord.put(f, f.lastModified());
				return "MODIFIED : " + f.getPath();
			} else
				return null;
		} else {
			fileRecord.put(f, f.lastModified());
			return "CREATED : " + f.getPath();
		}
	}

}
