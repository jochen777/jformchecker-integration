package de.jformchecker.adapter.request;

import javax.servlet.http.HttpServletRequest;

import de.jformchecker.request.Request;

/**
 * Adapter, that brings the HTTPServletRquest to the Request Interface
 * @author jochen
 *
 */
public class ServletRequestAdapter implements Request{

	HttpServletRequest servletRequest;
	
	public ServletRequestAdapter(HttpServletRequest request) {
		servletRequest = request;
	}
	
	@Override
	public String getParameter(String name) {
		return servletRequest.getParameter(name);
	}


	public static Request of(HttpServletRequest request) {
		return new ServletRequestAdapter(request);
	}
	
}
