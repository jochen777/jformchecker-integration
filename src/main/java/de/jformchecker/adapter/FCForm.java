package de.jformchecker.adapter;

/**
 * Container for a bean, that can be filled from the framework
 * @author jochen
 *
 * @param <T>
 */
public class FCForm<T> {
	
	Object model;
	FC fc;
	
	public void setModel(Object model) {
		this.model = model;
	}
	
	public T getModel() {
		return (T)model;
	}

	public void setFC(FC fc) {
		this.fc = fc;
	}

	public FC getFc() {
		return fc;
	}
	
	public String getHtml() {
		return fc.getFcInstance().getView().getForm();
	}
}
