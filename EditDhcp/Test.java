//import java.io.IOException;
import java.io.IOException;
import java.util.HashMap;
public class Test{
	public static void main(String[] args){
		
		
		String usage = "\n-c /path/to/configfile"
				+ " -ip <new ipaddress of the host>"
				+ " -mac <mac address of the pxe enabled interface>"
				+ " -fqdn <fqdn hostname of the new host>"
				+ " -route <the default gateway to be set to the host>"
				+ " -network <The default network this host belongs. eg:172.16.207.0>"
				+ " -netmask <The subnet of the network. eg:255.255.255.0>\n\n"
				+ " -h to print this help and exit";
		
		String conf_file = null;
		String ipAddress = null;
		String macAddress = null;
		String fqdn = null;
		String route = null;
		String network = null;
		String netmask = null;
		String include_conf = null; // Path to include file
		int ExitStatus = 0;
		
	//	System.out.println(System.getProperty("user.name"));
		GetOpt getoptions = new GetOpt(args, usage);
		try{
			getoptions.checkArgs();
			}
		catch(Exception e){
			System.out.println(getoptions.getHelper());
			System.out.println(e);
			Runtime.getRuntime().exit(1);
			}

		HashMap<String, String> hmap = getoptions.mapOptions();
		for(String key: hmap.keySet()){
			if(key.equals("-c")){
				conf_file = hmap.get(key);
			}
			else if(key.equals("-h")){
				System.out.println(getoptions.getHelper());
				Runtime.getRuntime().exit(0);
			}
			else if(key.equals("-ip")){
				ipAddress = hmap.get(key);
			}
			else if(key.equals("-mac")){
				macAddress = hmap.get(key);
			}
			else if(key.equals("-fqdn")){
				fqdn = hmap.get(key);
			}
			else if(key.equals("-route")){
				route = hmap.get(key);
			}
			else if(key.equals("-network")){
				network = hmap.get(key);
			}
			else if(key.equals("-netmask")){
				netmask = hmap.get(key);
			}
		}
		
		if(conf_file == null || route == null || network == null || ipAddress == null || 
				macAddress == null || netmask == null || fqdn == null){
			System.out.println("One of the mandatory fields are empty"
					+ "\n" + getoptions.getHelper());
			Runtime.getRuntime().exit(1);
		}

		
//Validate file existance
		try{
			LockFile.Validate(conf_file);
		}
		catch(Exception e){
			System.out.println(e);
			Runtime.getRuntime().exit(1);
		}
		
//Take Backup
		
		BackupConf backup = new BackupConf(conf_file);
			try{
				backup.takeBackup(backup.getPath());
			}
			catch(Exception E){
				System.out.println("Cannot take backup of" + backup.getBackupFilePath());
				System.out.println(E);
				Runtime.getRuntime().exit(1);
			}
				
	////			System.out.println(backup.getBackupFilePath());
		
//Create new File lock object

		LockFile lockfile = new LockFile(conf_file);
		
		try{
			lockfile.getLock(); //Acquires lock
			include_conf = UpdateConf.writeToIncludeFile(fqdn, macAddress,
					ipAddress, route, network, netmask); //Write to include file in /etc/dhcp/hosts
			UpdateConf.writeToMainConf(lockfile, include_conf); //write to main conf /etc/dhcp/dhcp.conf
			
			
			lockfile.purgeLock();
		}
		catch(Exception E){
			lockfile.purgeLock();
			System.out.println(E);
			Runtime.getRuntime().exit(1);
		}
		
//Create new Service Restart object		
		
		RestartService restart = new RestartService("/etc/init.d/apache2", "restart");
		
		try {
			ExitStatus = restart.Restart();
		} catch (IOException e) {
			System.out.println("Caught IO Exception while restarting service, check manually");
		} catch (InterruptedException e) {
			System.out.println("Interrupt caught while restarting. Check manually");
		}
		
		
//Create restore process of conf file if ExitStatus is not 0

		if(ExitStatus != 0){
			System.out.println("Restart failed with exit status " + ExitStatus + ". Initiating restore of conf files."
					+ " Check and validate the configuration. ");	
			RestoreBackup restore = new RestoreBackup(backup.getBackupFilePath(), conf_file, include_conf);
			restore.Restore();
			
		}
		else{
			System.out.println("Restart finished with exit status " + ExitStatus);
		}
	}

	
}