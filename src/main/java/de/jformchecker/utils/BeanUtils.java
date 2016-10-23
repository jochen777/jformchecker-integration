package de.jformchecker.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import de.jformchecker.FormCheckerElement;
import de.jformchecker.FormCheckerForm;
import de.jformchecker.elements.CheckboxInput;
import de.jformchecker.elements.DateInputCompound;
import de.jformchecker.elements.Label;
import de.jformchecker.elements.NumberInput;
import de.jformchecker.elements.TextInput;

/**
 * Helps working with Beans. Can transfer beans to forms and vice versa.
 * 
 * @author jochen
 *
 */
public class BeanUtils {

	public static FormCheckerForm fromBean(Object o) {
		FormCheckerForm f = new FormCheckerForm() {

			@Override
			public void init() {
				Map<String, Object> elements;
				try {
					PropertyDescriptor[] p = PropertyUtils.getPropertyDescriptors(o);
					for (PropertyDescriptor pd : p) {
						System.err.println("typ:" + pd.getPropertyType());
						System.err.println(pd.getDisplayName());
					}
					elements = PropertyUtils.describe(o);
					// elements.forEach((key, value) -> this.add(
					//
					// TextInput.build(key).setDescription(key).setPreSetValue(value.toString())
					//
					// ));

					for (Field f : o.getClass().getDeclaredFields()) {
						String name = f.getName();
						Object fieldValue = PropertyUtils.getProperty(o, f.getName());
						String description = name;
						Label label = f.getAnnotation(Label.class);
						if (label != null) {
							description = label.text();
						}
						FormCheckerElement el;
						if (fieldValue instanceof Boolean) {
							el = CheckboxInput.build(name).setDescription(description)
									.setPreSetValue(fieldValue.toString());
						} else if (fieldValue instanceof LocalDate) {	// We prefer the Java 8 API!
							LocalDate dateVal = (LocalDate)fieldValue; 
							el = DateInputCompound.build(name).presetValue(java.sql.Date.valueOf(dateVal)).setDescription(description);
					} else if (fieldValue instanceof Integer) {	
						Integer intVal = (Integer)fieldValue; 
						el = NumberInput.build(name).presetIntValue(intVal).setDescription(description);
					}
						else {
							el = TextInput.build(name).setDescription(description)
									.setPreSetValue(fieldValue.toString());

						}
						this.add(el);

					}

				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		return f;
	}

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

}
