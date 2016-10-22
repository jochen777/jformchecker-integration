package de.jformchecker.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import de.jformchecker.FormCheckerElement;
import de.jformchecker.FormCheckerForm;
import de.jformchecker.elements.CheckboxInput;
import de.jformchecker.elements.DateInputCompound;
import de.jformchecker.elements.Label;
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

}
