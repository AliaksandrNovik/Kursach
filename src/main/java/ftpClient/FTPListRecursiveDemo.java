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

public class FTPListRecursiveDemo {
	
	private static boolean isDirectory(FTPClient ftpClient, String parentDir) throws IOException{
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
	
	private static void listDirectory(FTPClient ftpClient, String parentDir,
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

	private static void printComandInform() {
		System.out.println("================================");
		System.out.println("Information about functions of program: ");
		System.out.println("/ - to the open head directory ");
		System.out.println("name of directory - to the open this directory");
		System.out.println("name of file - to the  download this file ");
		System.out.println("exit - to the exit from application ");
		System.out.println("================================");
	}

	private static void downloadFile(FTPClient ftpClient, String path,
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

	private static void connectToFtp(FTPClient ftpClient) throws IOException {
		String server = "v011777.home.net.pl";
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

	public static void main(String[] args) {
		String directoryOfSavingFile = args[0];
		printComandInform();
		FTPClient ftpClient = new FTPClient();
		try {
			connectToFtp(ftpClient);
			String currentName = "";
			String path = "";
			do {
				Scanner scanner = new Scanner(System.in);
				currentName = scanner.nextLine();
				path += currentName + "/";
				if (isDirectory(ftpClient, path)) {
					System.out.println(currentName + " " + path);
					listDirectory(ftpClient, path, "");
				} else {
					//add to archiv
				}
			} while (!currentName.equals("exit"));
		} catch (IOException ex) {
			System.out.println("Not found output folder");
			ex.printStackTrace();
		} finally {
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
}