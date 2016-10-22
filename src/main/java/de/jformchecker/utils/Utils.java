package de.jformchecker.utils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import de.jformchecker.FormCheckerElement;
import de.jformchecker.elements.DateInputCompound;

public class Utils {
	public static void fillBean(List<FormCheckerElement> elements, Object bean)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (FormCheckerElement elem : elements) {
			String key = elem.getName();
			if (PropertyUtils.isWriteable(bean, key)) {
				Object propertyVal = PropertyUtils.getSimpleProperty(bean, key);
				if (propertyVal instanceof String) {
					PropertyUtils.setSimpleProperty(bean, key, elem.getValue());
				} else if (propertyVal instanceof Boolean) {
					if ("true".equals(elem.getValue())) {
						PropertyUtils.setSimpleProperty(bean, key, true);
					} else {
						PropertyUtils.setSimpleProperty(bean, key, false);
					}
				} else if (propertyVal instanceof LocalDate) {
					if (elem instanceof DateInputCompound) {
						Date dateVal = ((DateInputCompound)elem).getDateValue();
						LocalDate dateValLocalDate = new java.sql.Date( dateVal.getTime() ).toLocalDate();
						PropertyUtils.setSimpleProperty(bean, key, dateValLocalDate);
					}
				}
				
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
