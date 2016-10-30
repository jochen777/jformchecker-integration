package de.jformchecker.utils;

import de.jformchecker.FormCheckerForm;

/**
 * Interface, that helps beans to be transformed to/from a FormcheckerForm
 * @author jochen
 *
 */
public interface FormCheckerBean {
	public void preRun(FormCheckerForm form) ;
	
	public void postRun(FormCheckerForm form);

}
