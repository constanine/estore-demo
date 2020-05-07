package com.bokesoft.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class NetEnvUtils {
	private static List<String> allowInnerServerIp;
	private static List<String> allowOutterServerIp = new ArrayList<String>();
	
    public static boolean isInnerNetReq(HttpServletRequest req) {
    	String reqUrl = req.getRequestURL().toString();
    	String serverIp = getDomain(reqUrl);
    	if(null == allowInnerServerIp || allowInnerServerIp.isEmpty()) {
    		allowInnerServerIp = new ArrayList<String>();
    		allowInnerServerIp = Arrays.asList(EnvUtils.getInIp().split(","));
    	}
    	if(allowInnerServerIp.contains(serverIp)) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean isOutterNetReq(HttpServletRequest req) {
    	String reqUrl = req.getRequestURL().toString();
    	String serverIp = getDomain(reqUrl);
    	if(null == allowOutterServerIp || allowOutterServerIp.isEmpty()) {
    		allowOutterServerIp = new ArrayList<String>();
    		allowOutterServerIp = Arrays.asList(EnvUtils.getOutIp().split(","));
    	}
    	if(allowOutterServerIp.contains(serverIp)) {
    		return true;
    	}
    	return false;
    }
    
	private static String getDomain(String url) {
		String regex = "^.*?://(?:www[.])?(\\w+([.]\\w+)*)(:\\w+)?/.*$";
		Matcher matcher = Pattern.compile(regex).matcher(url);
		String result = null;
		if (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}
}
