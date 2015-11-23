import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Formatter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class UpdateConf {
	/**
	 * 
	 * @param lockfile object. getFileObject in lockfile object is used to create a RandomAccessFile object.
	 * @param path that is returned by UpdateConf.writeToIncludeFile()
	 * @throws IOException
	 */
	
	public static void writeToMainConf(LockFile lockfile, String path) throws IOException{
		String conf_entry = String.format("%s%s%s", "include \"", path, "\";\n");
		System.out.println(conf_entry);
		RandomAccessFile f = lockfile.getFileObject();
		f.seek(f.length());
		System.out.println("Seeking to " + f.getFilePointer() + " byte on main configuration");
		f.writeBytes(conf_entry);
		System.out.println("Written " + conf_entry.getBytes().length + " bytes");
		System.out.println("Entry '" + conf_entry.trim() + "' added");

	}
	
	private static Formatter output;
	
	
	
	/**
	 * @return the output
	 */
	public static Formatter getOutput() {
		return output;
	}
	
	/**
	 * @param output the output to set
	 */
	public static void setOutput(Formatter output) {
		UpdateConf.output = output;
	}
	
	/**
	 * Write to the include file
	 * @param fqdn that will be the filename in the include path and replaceAll 
	 * dummyhostname in the template.
	 * @param macAddress that will replaceAll the dummymacid in the template.
	 * @param ipAddress that will replaceAll the dummpip in the template.
	 * @param route that will replaceAll the dummyroute in the template.
	 * @return the include file that has to be added to the main dhcp conf.
	 * @throws Exception
	 */
	public static String writeToIncludeFile(String fqdn, String macAddress,
			String ipAddress, String route, String network, String netmask) throws Exception{
		String path = "/etc/dhcp/hosts/" + fqdn + ".conf";
		System.out.println("Attempting to create file " + path);
		File confFile = new File(path);
		if(!confFile.exists()){
			UpdateConf.setOutput(new Formatter(confFile));
			StringBuffer template_read = UpdateConf.readFile("TemplateInclude.txt", true);
			template_read = new StringBuffer(template_read.toString().replaceAll("dummyhostname", fqdn));
			template_read = new StringBuffer(template_read.toString().replaceAll("dummymacid", macAddress));
			template_read = new StringBuffer(template_read.toString().replaceAll("dummyip", ipAddress));
			template_read = new StringBuffer(template_read.toString().replaceAll("dummyroute", route));
			template_read = new StringBuffer(template_read.toString().replaceAll("dummynetwork", network));
			template_read = new StringBuffer(template_read.toString().replaceAll("dummynetmask", netmask));
			System.out.println(template_read);
			RandomAccessFile f = new RandomAccessFile(path, "rw");
			FileChannel channel = f.getChannel();
			FileLock lock = channel.lock();
			if(lock != null && lock.isValid()){
				System.out.println("Aquired "+ lock + " on " + path);
				f.writeBytes(template_read.toString());
			}
			f.close();
			return path;
		}
		else{
			throw new Exception("File " + path + " already exists. Remove "
							+ path + " and the respective include directive "
							+ "from the main configuration file"
							+ "  /etc/dhcp/dhcp.conf if any, "
							+ "and try again.");
		}	
	}
	
	
	/**
	 * 
	 * @param template_conf which is the path to the file to read
	 * @param TemplateInClassPath Boolean value. true if file is in ClassPath, false if not.
	 * @return A StringBuffer object that can be used.
	 */
	
	public static StringBuffer readFile(String template_conf, boolean TemplateInClassPath){
		if(TemplateInClassPath == true){
			InputStream template = UpdateConf.class.getResourceAsStream(template_conf);
			System.out.println("Attempting to read from template file " + template_conf);
			InputStreamReader templateReader = new InputStreamReader(template);
			BufferedReader br = new BufferedReader(templateReader);
			StringBuffer sb = new StringBuffer();
			String line = null;
			try{
				while((line = br.readLine()) != null){
					sb.append(line).append("\n");
				}
				//System.out.println(sb);
			}
			catch(Exception E){
				System.out.println(E);
			}
			return sb;
		}
		else{
			FileInputStream template;
			StringBuffer sb = new StringBuffer();
			try {
				template = new FileInputStream(template_conf);
				System.out.println("Attempting to read from file " + template_conf);
				int content = template.available();
				System.out.println("Total file size to read (in bytes) : "
						+ content);
				while((content = template.read()) != -1){
					sb.append((char) content);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
			return sb;
		}

	}
	
}