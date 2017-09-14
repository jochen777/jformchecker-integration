package de.jformchecker.utils;

@SuppressWarnings("serial")
public class DoubleLabelException extends RuntimeException{
	public DoubleLabelException(String elementName) {
		super("You defined two Labels (Label AND LabelTranslationKey) on one element: '" + elementName + "' \nUse either @Label OR @LabelTranslationKey. Not both");
	}
}
