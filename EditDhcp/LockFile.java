//Validates and acquires a filelock.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class LockFile {
	
	private final String conf_file;
	private File lockFile;
	private RandomAccessFile f;
	private FileChannel channel;
	private FileLock lock;
	//End of instance variable declaration
	
	//Static method to validate the conf file path and is writable.
	public static void Validate(String conf_file) throws IOException{
		Path path = Paths.get(conf_file);
		if(Files.exists(path) && Files.isWritable(path) && !(Files.isDirectory(path))){
			
		}
		else{
			throw new IOException("Check "
					+ path + " is a file, it exists and have write permissions");
		}		
	}
	
	public  LockFile(String conf_file){
		this.conf_file = conf_file;
	}
		
	public boolean getLock() throws FileNotFoundException, IOException{	
		lockFile = new File(conf_file);
		System.out.println("Attempting to aquire"
				+ " exclusive lock on " + conf_file);
		f = new RandomAccessFile(lockFile, "rw");
		channel = f.getChannel();
		lock = channel.lock();
		if(lock != null && lock.isValid()){
			System.out.println("Aquired "+ lock + " on " + lockFile);
			return true;
		}
		else{
			return false;
		}
	}
	
	public RandomAccessFile getFileObject(){
		return f;
	}
	
	public void purgeLock() throws IOException {
		System.out.println("Removing " + lock);
		f.close();
		channel.close();
	}

}
