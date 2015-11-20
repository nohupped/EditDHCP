/*Accepts arguments, can validate it and return true if valid, and
 * return a HashMap of parameter as key and argument as value
 * Eg: -c /path/to/conf will return as a hashmap of 
 * -c=/path/to/conf */

import java.util.Arrays;
import java.util.HashMap;
public class GetOpt {
	
	private String[] args;
	
	public GetOpt(String[] args){
		for(int count=0; count < args.length; count+=2){
			if(!args[count].startsWith("-")) {
				throw new IllegalArgumentException("Argument starts with '-'");
			}
		}
		this.args = args;
	}
	
	//Check argument syntax
	public static boolean checkArgs(String[] args){
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
	
	/*public void setOptions(String[] args){
		for(int count=0; count < args.length; count+=2){
			if(!args[count].startsWith("-")) {
				throw new IllegalArgumentException("Argument starts with '-'");
				}
			}
		this.args = args;
		}*/
	
	public String[] getOptions(){
		return args;
	}
	
	public HashMap<String, String> mapOptions(){
		HashMap<String, String> hmap = new HashMap<String, String>();
		for(int i=0; i<args.length; i+=2){
			try{
				hmap.put(args[i], args[i+1]);
				}
			catch(Exception e){
				System.out.println("Option syntax mismatch, please crosscheck"
						+ " the options again\n" + e);
			}
		}
		return hmap;
	}
	
	
	@Override
	public String toString(){
		return String.format("%s", Arrays.toString(getOptions()));
	}
	
	
	
}
