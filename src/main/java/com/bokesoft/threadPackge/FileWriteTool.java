package com.bokesoft.threadPackge;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.bokesoft.config.ApplicationConfiguer;
import com.bokesoft.utils.EnvUtils;

class FileWirterTool {

	/**
	 * @param args
	 * @targetUrl为文件复制目标地址
	 * @sourceUrl为文件源头地址
	 * @isFinish表示copy是否順利完成
	 * @why表示Exception原因
	 */

	private boolean isFinish = true;
	private String why;
	private long filesize;
	private Logger logger;

	public boolean getIsFinish() {
		return isFinish;
	}

	public String getWhy() {
		return why;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @method 这里通过targetUrl，sourceUrl传参，将文件按目标要求复制
	 * @catch 如果放生异常，则isFinish为false
	 */
	
	public void moveFileAndFolder(String sourceUrl, String targetUrl) {

		File sourceFile = new File(sourceUrl);

		File destFile = new File(targetUrl);
		
		try {
			logger.info(">>>Copying.... from " + sourceUrl + " to " + targetUrl);
			filesize = sourceFile.length();
			if(sourceFile.isDirectory()){
				FileUtils.moveDirectory(sourceFile, destFile);
			}else{				
				FileUtils.moveFile(sourceFile, destFile);
			}
			logger.info(">>>"+sourceUrl+"is moved");
		} catch (Exception e) {
			isFinish = false;
			why ="Message:"+  e.getMessage() + ",Cause:" +getStackTrace(e) ;
		}
		

	}
	
	public void copyFileAndFolder(String sourceUrl, String targetUrl) {

		File sourceFile = new File(sourceUrl);

		File destFile = new File(targetUrl);
		
		try {
			logger.info(">>>Copying.... from " + sourceUrl + " to " + targetUrl);
			filesize = sourceFile.length();
			if(sourceFile.isDirectory()){
				FileUtils.copyDirectory(sourceFile, destFile);
			}else{				
				FileUtils.copyFile(sourceFile, destFile);
			}
			logger.info(">>>"+sourceUrl+"is copyed");
		} catch (Exception e) {
			isFinish = false;
			why ="Message:"+  e.getMessage() + ",Cause:" +getStackTrace(e) ;
		}
		

	}
	
	/**
	 * @method 通过sourceUrl将删除sourceUrl下所有的文件及子目录
	 * @param dir 被Directory的文件路径
  	 * @return 目录删除success返回true,否则返回false
  	 */
  	public boolean clearSourceUrl(String dir, String userCode, ApplicationConfiguer configuer) {
  		boolean flag = true;
  		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
  		if (!dir.endsWith(File.separator)) {
  			dir = dir + File.separator;
  		}
  		File dirFile = new File(dir);
  		// 如果dir对应的文件不存在，或者不是一个目录，则退出
  		if (!dirFile.exists() || !dirFile.isDirectory()) {
  			logger.warn("Directoryfalied" + dir + "目录不存在！");
  		}

  		// 删除文件夹下的所有文件(包括子目录)
  		File[] files = dirFile.listFiles();
  		for (int i = 0; i < files.length; i++) {
  			// 删除子文件
  			if (files[i].isFile()) {
  			
  			}
  			// 删除子目录
  			else {
  				flag = clearSourceUrl(files[i].getAbsolutePath(), userCode,configuer);
  				if (!flag) {
  					break;
  				}
  			}
  		}

  		if (!flag) {
  			logger.warn("Directoryfalied");
  			return false;
  		}

  		if (dir.equals(EnvUtils.getO2ISourceDir(userCode))) {
  			return false;
  		} else {
  			if (dirFile.delete()) {
  				logger.info("clear Directory:" + dir + "success！");
  				return true;
  			} else {
  				logger.warn("clear Directory:" + dir + "falied！");
  				return false;
  			}
  		}
  	}
	
  	
  	public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try
        {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally
        {
            pw.close();
        }
    }
	
}