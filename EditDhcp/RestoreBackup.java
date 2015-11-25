import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;



public class RestoreBackup {

	private File source, destination, includeToRemove;
	
	public RestoreBackup(String source, String destination, String includeToRemove){
		this.source = new File(source);
		this.destination = new File(destination);
		this.includeToRemove = new File(includeToRemove);
	}
	
	public void Restore(){
		try{
			System.out.println(source.toPath() + "--->" + destination.toPath());
			Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Deleting include file " + includeToRemove.toString());
			includeToRemove.delete();
			
		}
		catch(Exception E){
			System.out.println("Error restoring backup. Please do it manually. Files are ");
			System.out.println("Backup file: " + source);
			System.out.println("Original file: " + destination);
			System.out.println("Include file top be removed:" + includeToRemove);
			E.printStackTrace();
		}
	}
}
