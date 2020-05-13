package com.bokesoft.ecomm.estore.web.filter;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.bokesoft.ecomm.estore.mid.util.EstoreContext;
import com.bokesoft.ecomm.estore.mid.util.EstoreContextBuilder;

/**
 * Servlet Filter implementation class SessionFilter
 */
public class RequsetInitFilter implements Filter {
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		EstoreContext context = null;
		try {
			context = EstoreContextBuilder.get();
			chain.doFilter(request, response);
			context.getDBManager().commit();
		} catch (Throwable e) {
			if(null != context) {
				try {
					context.getDBManager().rollback();
				} catch (SQLException e1) {
					logger.error(e1.getMessage(), e1);
				}
			}
			logger.error(e.getMessage(), e);
			throw new ServletException(e);
		} finally {
			if (null != context) {
				try {
					context.getDBManager().close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

}
