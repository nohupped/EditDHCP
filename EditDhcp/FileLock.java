//Validates and acquires a filelock.
import java.io.IOException;
//import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.io.File;

public class FileLock {
	public static void Validate(String conf_file) throws IOException{
		Path path = Paths.get(conf_file);
		if(Files.exists(path) && Files.isWritable(path)){
			
		}
		else{
			throw new IOException("Check "
					+ path + " file exists and have write permissions");
		}
	}

}
