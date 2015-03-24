package ftpClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpWork {
	public  void printComandInform() {
		System.out.println("================================");
		System.out.println("Information about functions of program: ");
		System.out.println("/ - to the open head directory ");
		System.out.println("name of directory - to the open this directory");
		System.out.println("name of file - to the  download this file ");
		System.out.println("exit - to the exit from application ");
		System.out.println("================================");
	}
	public String readFromConsole(String currentName){
		Scanner scanner = new Scanner(System.in);
		currentName = scanner.nextLine();
		return currentName;
	}
	public  boolean isDirectory(FTPClient ftpClient, String parentDir) throws IOException{
		int cntInnerFiles = 0;;
		FTPFile[] subFiles = ftpClient.listFiles(parentDir);
		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				if (aFile.isDirectory() || aFile.isFile()) {
					cntInnerFiles++;
				}
			}
		}
		if (cntInnerFiles >= 1){
			return true;}
		else {
			return false;}
	}
	
	public  void listDirectory(FTPClient ftpClient, String parentDir,
			String currentDir) throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}
		FTPFile[] subFiles = ftpClient.listFiles(dirToList);
		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".")
						|| currentFileName.equals("....")) {
					continue;
				}
				if (aFile.isDirectory() || aFile.isFile()) {
					System.out.println(currentFileName);
				}
			}
		}
	}
	public  void connectToFtp(FTPClient ftpClient) throws IOException {
		String server = "ftp.mozilla.org";
		int port = 21;
		String user = "anonymous";
		String pass = "123";
		ftpClient.connect(server, port);
		int replyCode = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			System.out.println("Connection failed");
			return;
		}
		boolean success = ftpClient.login(user, pass);
		if (!success) {
			System.out.println("Could not login to to the server");
			return;
		}

	}
	@SuppressWarnings("unused")
	public  void downloadFileFromFtp(FTPClient ftpClient, String path,
			String currentName, String directoryOfSavingFile)
			throws IOException {
		final int partStream = 4096;
		String remoteFile1 = path.substring(0, path.length() - 1);
		File downloadFile1 = new File(directoryOfSavingFile + currentName);
		OutputStream outputStream2 = new BufferedOutputStream(
				new FileOutputStream(downloadFile1));
		InputStream inputStream = ftpClient.retrieveFileStream(remoteFile1);
		byte[] bytesArray = new byte[partStream];
		int bytesRead = -1;
		System.out.println("Load of " + currentName + "...");
		while ((bytesRead = inputStream.read(bytesArray)) != -1) {
			outputStream2.write(bytesArray, 0, bytesRead);
		}
		boolean success = ftpClient.completePendingCommand();
		if (success) {
			System.out.println("File " + currentName
					+ " has been downloaded successfully.");
		}
		outputStream2.close();
		inputStream.close();
	}
	public  void disconnectFromFtp(FTPClient ftpClient){
		try {
			if (ftpClient.isConnected()) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
