package de.jformchecker.utils;

import java.time.LocalTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.jformchecker.FormCheckerElement;

public class Utils {
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
