package com.oxchains.themis.order.common;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Controller;
@Controller
public class FtpUtils {
	private static FTPClient ftp = new FTPClient();
	private static String host="192.168.1.195";
	private static String username="ftpuser";
	private static String password="ftpuser";
	private static String workdir="/home/ftpuser/images/image";
	private static Integer port=21;
/*	static{
		try {
			ftp.setDataTimeout(60000);
			ftp.setConnectTimeout(60000);
			ftp.connect(host,port);
			ftp.login(username, password);
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(workdir);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	public static void uploadImage(InputStream inputStream,String imagename){
		try {
				ftp.setDataTimeout(60000);
				ftp.setConnectTimeout(60000);
				ftp.connect(host,port);
				ftp.login(username, password);
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				ftp.changeWorkingDirectory(workdir);
			ftp.storeFile(imagename,inputStream );
			inputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public FtpUtils() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
