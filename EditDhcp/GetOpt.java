/*Accepts arguments, can validate it and return true if valid, and
 * return a HashMap of parameter as key and argument as value
 * Eg: -c /path/to/conf will return as a hashmap of 
 * -c=/path/to/conf */

import java.util.Arrays;
import java.util.HashMap;
public class GetOpt {

	private String[] args;
	private String usage;
	//Constructor accepting Args and Helper usage
	public GetOpt(String[] args, String usage){
		this.usage = usage;
		this.args = args;
	}
	//Constructor accepting Args only
	public GetOpt(String[] args){
		this.args = args;
	}
	//Set helper usage
	public void setHelper(String usage){
		this.usage = usage;
	}
	//Check argument validity
	public boolean checkArgs() throws Exception{
		if (args.length < 1){
			throw new IllegalArgumentException("Zero Argument length");
		}
		for(int count=0; count < args.length; count+=2){
			if(!args[count].startsWith("-")) {
				throw new IllegalArgumentException("Argument starts with '-'");
			}
		}
		return true;
	}

	
	public String[] getOptions(){
		return args;
	}
	
	public String getHelper(){
		return "Usage: " + usage;
	}

	public HashMap<String, String> mapOptions(){
		HashMap<String, String> hmap = new HashMap<String, String>();
			for(int i=0; i<args.length; i+=2){
				try{
					hmap.put(args[i], args[i+1]);
					}
				catch(Exception e){
					hmap.put(args[i], null);
				}
			}


		return hmap;
	}
	
	
	@Override
	public String toString(){
		return String.format("%s", Arrays.toString(getOptions()));
	}
	
}