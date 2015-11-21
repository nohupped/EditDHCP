import java.io.IOException;
import java.io.RandomAccessFile;

public class UpdateConf {
		
	public static void writeToMainConf(LockFile lockfile, String fqdn) throws IOException{
		String conf_entry = String.format("%s%s%s", "include "
				+ "\"/etc/dhcp/hosts/", fqdn, ".conf\";\n");
		RandomAccessFile f = lockfile.getFileObject();
		f.seek(f.length());
		System.out.println(f.getFilePointer());
		f.writeBytes(conf_entry);

	}
	
}
