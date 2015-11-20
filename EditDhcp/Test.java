import java.io.IOException;
import java.util.HashMap;
public class Test{
	public static void main(String[] args){
		
		String conf_file = null;
		
		System.out.println(GetOpt.checkArgs(args));
		GetOpt getoptions = new GetOpt(args);
		HashMap<String, String> hmap = getoptions.mapOptions();
		for(String key: hmap.keySet()){
			if(key.equals("-c")){
				conf_file = hmap.get(key);
			}
		}

		try{
			FileLock.Validate(conf_file);
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
}