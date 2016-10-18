package de.jformchecker.adapter.request;

import javax.servlet.http.HttpServletRequest;

import de.jformchecker.request.Request;
import de.jformchecker.request.Session;

/**
 * Adapter, that brings the HTTPServletRquest to the Request Interface
 * @author jochen
 *
 */
public class ServletRequestAdapter implements Request{

	HttpServletRequest servletRequest;
	Session session;
	
	public ServletRequestAdapter(HttpServletRequest request) {
		servletRequest = request;
		session = new HttpSessionAdapter(request.getSession());
	}
	
	@Override
	public String getParameter(String name) {
		return servletRequest.getParameter(name);
	}

	@Override
	public Session getSession() {
		return session;
	}

	public static Request of(HttpServletRequest request) {
		return new ServletRequestAdapter(request);
	}
	
}
