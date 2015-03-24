package ftpClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipOutputStream;

import org.apache.commons.net.ftp.FTPClient;


public class RunAplication {
	static Properties property = new Properties();
	public static void loadProperty(){
		
	    try{
	    	FileInputStream file = new FileInputStream("src/main/java/ftpClient/resource/config.properties");
	    	property.load(file);
	    	
	    }catch(IOException fn){
	    	System.out.println("Not find config.properties");
	    }
	}
	
	@SuppressWarnings({ "unused", "static-access" })
	public static void main(String[] args) throws IOException{
		
		FileOutputStream fos = new FileOutputStream("Output.zip");
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		FtpWork ftp = new FtpWork();
		FTPClient ftpClient = new FTPClient();
		ZipArch zip = new ZipArch();
		loadProperty();
		String directoryOfSavingFile = property.getProperty("OutputFolder");
		ftp.printComandInform();
		
		try {
			ftp.connectToFtp(ftpClient);
			String currentName = "";
			String path = "" ;
			do {
				currentName = ftp.readFromConsole(currentName);
				path += currentName + "/";
				if (ftp.isDirectory(ftpClient, path)) {
					System.out.println(currentName + " " + path);
					ftp.listDirectory(ftpClient, path, "");
				} else {
					ftp.downloadFileFromFtp(ftpClient, path, currentName, directoryOfSavingFile);
					zip.addToZipFile(currentName, zos);
				}
			} while (!currentName.equals("exit"));
		}  finally {
			ftp.disconnectFromFtp(ftpClient);
		}
	}
}

