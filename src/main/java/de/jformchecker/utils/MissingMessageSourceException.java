package de.jformchecker.utils;

@SuppressWarnings("serial")
public class MissingMessageSourceException extends RuntimeException{
	public MissingMessageSourceException() {
		super("You did not provide a message Source to bean-utils. "
				+ "If you want to use LabelTranslationKey, you have to provide a message Source. ->BeanUtils.fromBean(bean, messageSource)");
	}
}
