//import java.io.IOException;
import java.util.HashMap;
public class Test{
	public static void main(String[] args){
		
		String usage = "-c /path/to/configfile"
				+ " -ip <new ipaddress of the host>"
				+ " -mac <mac address of the pxe enabled interface>"
				+ " -fqdn <fqdn hostname of the new host> \n"
				+ " -h to print this help and exit";
		String conf_file = null;
		String ipAddress = null;
		String macAddress = null;
		String fqdn = null;
		

		GetOpt getoptions = new GetOpt(args, usage);
		try{
			getoptions.checkArgs();
			}
		catch(Exception e){
			System.out.println(getoptions.getHelper());
			System.out.println(e);
			}

		HashMap<String, String> hmap = getoptions.mapOptions();
		for(String key: hmap.keySet()){
			if(key.equals("-c")){
				conf_file = hmap.get(key);
			}
			else if(key.equals("-h")){
				System.out.println(getoptions.getHelper());
				//Runtime.getRuntime().exit(0);
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
		}
		
//		System.out.println(conf_file == null);
//		System.out.println(conf_file);
		if(conf_file == null || ipAddress == null || 
				macAddress == null || fqdn == null){
			System.out.println("One of the mandatory fields are empty"
					+ "\n" + getoptions.getHelper());
			Runtime.getRuntime().exit(1);
		}

		try{
			FileLock.Validate(conf_file);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}