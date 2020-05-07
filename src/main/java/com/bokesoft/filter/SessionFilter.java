package com.bokesoft.filter;

import com.bokesoft.controller.SerachFileController;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet Filter implementation class SessionFilter
 */
public class SessionFilter implements Filter {
	public static final String ATTR_NO_AUTHED_URLS = "no-authed-urls";
	
	String noAuthedUrls[];

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String path = req.getServletPath();
		if (isInArray(path, noAuthedUrls)) {
			chain.doFilter(request, response);
		} else {
			if (StringUtils.isNotBlank((CharSequence) session.getAttribute(SerachFileController.USERCODE))) {
				chain.doFilter(request, response);
			} else {
				res.sendRedirect(req.getContextPath()+"/login");
			}
		}
	}

	private boolean isInArray(String path, String nofilterFiles[]) {
		for (int i = 0; i < nofilterFiles.length; i++) {
			String nofilterFile = nofilterFiles[i];
			if(nofilterFile.endsWith("*")){
				boolean itmatch =isMatchwithfolder(nofilterFile,path);
				if (itmatch) {					
					return true;
				}
			}else{
				if (nofilterFile.equals(path)) {
					return true;
				}
			}
		}
		return false;
	} 

	private boolean isMatchwithfolder(String nofilterFile,String path){
		String[] authfolders =nofilterFile.split("/");
		String[] reqfolders =path.split("/");
		String authfolder ="";
		String reqfolder="";
		for(int i=0;i<authfolders.length-1;i++){
			authfolder += authfolders[i];
		}
		
		for(int i=0;i<reqfolders.length-1;i++){
			reqfolder += reqfolders[i];
		}
		
		if(authfolder.equals(reqfolder)){
			return true;
		}
			
		return false;
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */

	public void init(FilterConfig config) throws ServletException {
		noAuthedUrls = config.getInitParameter(ATTR_NO_AUTHED_URLS).split(",");
	}


}
