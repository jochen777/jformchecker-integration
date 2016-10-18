package de.jformchecker.adapter.request;

import javax.servlet.http.HttpSession;

import de.jformchecker.request.Session;

/**
 * Adapter for http-Sesions from the servlet API
 * 
 * @author jochen
 *
 */
public class HttpSessionAdapter implements Session {

	HttpSession session;

	public HttpSessionAdapter(HttpSession session) {
		this.session = session;
	}

	@Override
	public Object getAttribute(String name) {
		return session.getAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object o) {
		session.setAttribute(name, o);
	}

}
