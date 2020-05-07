package com.bokesoft.utils;

import org.apache.log4j.*;

import java.io.File;
import java.io.IOException;

public class RunLogUtils {
	/**
	 * @param logsFolder
	 */
	static String logsFolder;

	public static String getLogsFolder() {
		return logsFolder;
	}

	private static void setLogsFolder(String logsFolder) {
		RunLogUtils.logsFolder = logsFolder;
	}
	/**
	 * @method prepareLogsFolder
	 * @param folderUrl
	 * @return logger 
	 * 通过prepareLogsFolder 创建一个运行日志。并存放在EnvUtils分配的
	 * 的路径里
	 */
	public static Logger prepareLogsFolder(String userCode, String folderUrl, String missionid, String missionType) {
		File folder = new File(folderUrl);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		setLogsFolder(folder.getPath());
		return createLogger(userCode,missionid,missionType);
	}

	/**
	 * @method createLogger 
	 * @param userCode
	 * @param missionid
	 * @return 通过参数user,missionid创建对应运行日志并存放在对应的文件夹,
	 * 通过EnvUtils获取对应的存放路径
	 */
	private static Logger createLogger(String userCode, String missionid, String missionType) {

		String name = RunLogUtils.class.getName() + "." + userCode;
		String runlogurl = "";
		if("Import".equals(missionType)){
			runlogurl= logsFolder
				+ File.separator + missionid+"-Import-runlog.txt";
		}else if("Scanner".equals(missionType)){
			runlogurl= logsFolder
					+ File.separator + missionid+"-Scanner-runlog.txt";
		}else if("To_Out".equals(missionType)){
			runlogurl= logsFolder
					+ File.separator + missionid+"-To_Out-runlog.txt";
		}else if("To_Tran".equals(missionType)){
			runlogurl= logsFolder
					+ File.separator + missionid+"-To_Tran-runlog.txt";
		}else if("Tran_Back".equals(missionType)){
			runlogurl= logsFolder
					+ File.separator + missionid+"-Tran_Back-runlog.txt";
		}else if("Authorized".equals(missionType)){
			runlogurl= logsFolder
					+ File.separator + missionid+"-Authorized-log.txt";
		}
		Logger logger = Logger.getLogger(name);
		logger.setAdditivity(false);
		logger.setLevel(Level.DEBUG);
		Layout layout = new SimpleLayout();
		Appender appender = null;
		try {
			appender = new FileAppender(layout, runlogurl);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		logger.removeAllAppenders();
		logger.addAppender(appender);
		return logger;
	}

}
