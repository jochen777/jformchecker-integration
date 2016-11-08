package de.jformchecker.adapter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import de.jformchecker.FormChecker;
import de.jformchecker.FormCheckerConfig;
import de.jformchecker.FormCheckerElement;
import de.jformchecker.FormCheckerForm;

/**
 * Facade for formchecker for most easy use in standard situations
 * @author jochen
 *
 */
public class FC {

	FormChecker fcInstance;
	
	public static FC simple(FormCheckerConfig config, HttpServletRequest request, FormCheckerForm form) {
		FC fc = new FC();
		fc.fcInstance = FormChecker.build("id", key -> request.getParameter(key), form)
				.run();
		return fc;
	}
	
	public static FC simpleFromBean(FormCheckerConfig config, Object bean, FormCheckerForm form) {
		FC fc = new FC();
			fc.fcInstance = FormChecker.build("id", key -> {
				
				try {
					return ""+PropertyUtils.getProperty(bean, key);
				} catch (Exception e) {
					return "";
				}
			}
				
			, form)
					.run();
		
		return fc;
	}

	public boolean isOk() {
		if (fcInstance.isValidAndNotFirstRun()) {
			return true;
		}
		return false;
	}
	
	public List<FormCheckerElement> getFieldList() {
		return fcInstance.getForm().getElements();
	}
	
	public Map<String, FormCheckerElement> getFields() {
		return fcInstance.getForm().getElementsAsMap();
	}
	
	public String getValueFor(String fieldName) {
		return fcInstance.getForm().getElementsAsMap().get(fieldName).getValue();
	}
	
	public static FC secure(FormCheckerConfig config, HttpServletRequest request, FormCheckerForm form) {
		FC fc = new FC();

		fc.fcInstance = FormChecker.build("id", key -> request.getParameter(key), form).
				setProtectAgainstCSRF(
				key -> request.getSession().getAttribute(key), 
				(k,v)-> request.getSession().setAttribute(k, v)
				).setConfig(config)
				.run();

		
		return fc;
	}

	public FormChecker getFcInstance() {
		return fcInstance;
	}
}
