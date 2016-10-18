package de.jformchecker.adapter.message;

import java.util.ResourceBundle;

import de.jformchecker.message.MessageSource;

public class ResourceBundleMessageSource implements MessageSource {

	ResourceBundle bundle;

	public ResourceBundleMessageSource(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public String getMessage(String key) {
		if (this.bundle.containsKey(key)) {
			return bundle.getString(key);
		} else {
			System.err.println("bundle key is missing: " + key);
			return "";
		}
	}

}
