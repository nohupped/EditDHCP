import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;
import java.util.Formatter;

//Take backup of the main configuration and restore it if something fails.
public class BackupConf {
	private String mainConf;
	private Date date = new Date();
	private final Long uniqnum = date.getTime();
	private String backupPath;
	
	public BackupConf(String mainConf){
		this.mainConf = mainConf;
	}
	
	public String getPath(){
		return mainConf;
	}
	
	public void takeBackup(String mainConf) throws IOException{
		StringBuffer sourceConf = UpdateConf.readFile(mainConf, false);
		backupPath = mainConf + "." + uniqnum.toString();
		System.out.println("Backing up main file " + mainConf + " to " + backupPath);
		UpdateConf.setOutput(new Formatter(backupPath));
		RandomAccessFile f = new RandomAccessFile(backupPath, "rw");
		FileChannel channel = f.getChannel();
		FileLock lock = channel.lock();
		if(lock != null && lock.isValid()){
			System.out.println("Aquired "+ lock + " on " + backupPath);
			f.writeBytes(sourceConf.toString());
			f.close();
		}
		else{
			f.close();
			throw new IOException("Cannot complete backup, quitting.");
		}
		
		
	}
	
	public String getBackupFilePath(){
		return backupPath;
	}

}
