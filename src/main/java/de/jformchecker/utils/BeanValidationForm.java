package de.jformchecker.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.apache.commons.beanutils.PropertyUtils;

import de.jformchecker.FormCheckerElement;
import de.jformchecker.FormCheckerForm;
import de.jformchecker.elements.CheckboxInput;
import de.jformchecker.elements.DateInputCompound;
import de.jformchecker.elements.Label;
import de.jformchecker.elements.NumberInput;
import de.jformchecker.elements.TextInput;

public class BeanValidationForm extends FormCheckerForm{

	Object connectedBean;
	
	public BeanValidationForm(Object bean){
		this.connectedBean = bean;
	}
	
	@Override
	public void init() {
		try {
			PropertyDescriptor[] p = PropertyUtils.getPropertyDescriptors(connectedBean);
			for (PropertyDescriptor pd : p) {
				System.err.println("typ:" + pd.getPropertyType());
				System.err.println(pd.getDisplayName());
			}

			for (Field f : connectedBean.getClass().getDeclaredFields()) {
				String name = f.getName();
				Object fieldValue = PropertyUtils.getProperty(connectedBean, f.getName());
				String description = name;
				Label label = f.getAnnotation(Label.class);
				if (label != null) {
					description = label.text();
				}
				FormCheckerElement el;
				if (fieldValue instanceof Boolean) {
					el = CheckboxInput.build(name).setDescription(description)
							.setPreSetValue(fieldValue.toString());
				} else if (fieldValue instanceof LocalDate) { // We
																// prefer
																// the
																// Java
																// 8
																// API!
					LocalDate dateVal = (LocalDate) fieldValue;
					el = DateInputCompound.build(name).presetValue(java.sql.Date.valueOf(dateVal))
							.setDescription(description);
				} else if (fieldValue instanceof Integer) {
					Integer intVal = (Integer) fieldValue;
					el = NumberInput.build(name).presetIntValue(intVal).setDescription(description);
				} else {
					el = TextInput.build(name).setDescription(description)
							.setPreSetValue(fieldValue.toString());

				}
				this.add(el);

			}
			
			addFormValidator(new BeanValidationFormValidator(connectedBean));

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
