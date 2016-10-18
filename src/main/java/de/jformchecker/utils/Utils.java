package de.jformchecker.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import de.jformchecker.FormCheckerElement;

public class Utils {
	public static void fillBean(List<FormCheckerElement> elements, Object bean)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (FormCheckerElement elem : elements) {
			String key = elem.getName();
			if (PropertyUtils.isWriteable(bean, key)) {
				// RFE: Destinguish between Strings/Dates/Boolean...
				PropertyUtils.setSimpleProperty(bean, key, elem.getValue());
			}
		}
	}

	
	/**
	 * Return a nicely formated form of the form for debugging or other purposes
	 */
	public static String getDebugOutput(Map<String, FormCheckerElement> elements) {
		int maxLen = 0;
		for (String key : elements.keySet()) {
			if (key.length() > maxLen) {
				maxLen = key.length();
			}
		}
		maxLen += 3;
		StringBuilder debugOutput = new StringBuilder();
		for (String key : elements.keySet()) {

			debugOutput.append(key).append(StringUtils.leftPad(":", maxLen - key.length()));
			debugOutput.append(elements.get(key).getValue()).append("\n");
		}
		return debugOutput.toString();
	}

	
}
