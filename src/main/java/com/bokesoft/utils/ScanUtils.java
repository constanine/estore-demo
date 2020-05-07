package com.bokesoft.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
public class ScanUtils {

	public static boolean decideToAutoMove(List<String> copyfilelist, String userCode, String missionid) {
		Logger logger = Logger.getLogger(ScanUtils.class);
		int sumsize=0;
		for(String str:copyfilelist){
			String[] tmp = str.split("\t");
			String filename = tmp[1];
			String filepath = EnvUtils.getTransferDisk() + File.separator + userCode + File.separator + missionid + File.separator + filename;
			File file=new File(filepath);
			if (!isValidFile(file)) {
				return false;
			}
			sumsize+=file.length();
		}
		logger.info(">>>sumsize:"+sumsize);
		if((copyfilelist.size()<4)&&(sumsize<1024*10)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断文件是否是有效的文件
	 * @param file
	 * @return
	 */
	private static boolean isValidFile(File file) {
		if (!file.exists() || file.isDirectory()) {
			return false;
		}
		return isValidFileType(file);
	}

	/**
	 * 判断文件类型是否正确
	 * @param file
	 * @return
	 */
	private static boolean isValidFileType(File file) {
 		String fileType;
		try {
			fileType = getFileType(file);
		} catch (IOException e) {
			Logger logger = Logger.getLogger(ScanUtils.class);
			logger.error("Catch file type error,File:" + file.getAbsolutePath(), e);
			return false;
		}
		if (fileType.indexOf("UTF-8 Unicode text") >= 0) {
			return true;
		}
		if (fileType.indexOf("XML document text") >= 0) {
			return true;
		}
		if (fileType.indexOf("ASCII text") >= 0) {
			return true;
		}
		if (fileType.indexOf("ISO-8859 text") >= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 取文件类型,利用linux中的file命令来帮忙判断文件类型
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	private static String getFileType(File file) throws IOException {
		String filePath = file.getAbsolutePath();
		String command = "file \"" + filePath+"\"";
		Process process = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String code = br.readLine();
		br.close();
		Logger logger = Logger.getLogger(ScanUtils.class);
		logger.info(code);
		int pos = code.indexOf(":", filePath.length());
		return code.substring(pos + 1);
	}
}
