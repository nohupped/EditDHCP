import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RestartService {
	
	private String serviceInitScript;
	private String Command;
	
	/**
	 * 
	 * @param serviceInitScript (Path to the init script)
	 * @param Command (Specify stop/start/restart)
	 */
	
	public RestartService(String serviceInitScript, String Command){
		Path Init = Paths.get(serviceInitScript);
		if(Files.exists(Init) && !(Files.isDirectory(Init))){
			this.serviceInitScript = serviceInitScript.toString();
			this.Command = Command;
		}
		else{
			try {
				throw new IOException("Check if " + serviceInitScript + " exists");
			} catch (IOException e) {
				System.out.println("I/O error with init script. Please check manually.");
			}
		}
	}

	/**
	 * 
	 * @return int exit status of restart process
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	public int Restart() throws IOException, InterruptedException{
		String run = serviceInitScript + " " + Command;
		Process p = Runtime.getRuntime().exec(run);
		p.waitFor();
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String s;
		while ((s = stdInput.readLine()) != null) {
	        System.out.println(s);
		}
		int exitStatus = p.exitValue();
		return exitStatus;
	}
	
}
