package de.jformchecker.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jformchecker.FormCheckerElement;
import de.jformchecker.FormCheckerForm;
import de.jformchecker.elements.CheckboxInput;
import de.jformchecker.elements.DateInputCompound;
import de.jformchecker.elements.IgnoreFormElement;
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
	
	final static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

	public static FormCheckerForm fromBean(Object o) {
		FormCheckerForm f = new FormCheckerForm() {

			private Object connectedBean;

			@Override
			public void init() {
				connectedBean = o;
				try {

					for (Field f : o.getClass().getDeclaredFields()) {
						// check if it is ignored
						IgnoreFormElement ignored = f.getAnnotation(IgnoreFormElement.class);
						if (ignored != null) {
							continue;
						}
						
						
						String name = f.getName();
						Object fieldValue = PropertyUtils.getProperty(o, f.getName());
						String description = name;
						Label label = f.getAnnotation(Label.class);
						if (label != null) {
							description = label.text();
						}
						FormCheckerElement el;
						if (fieldValue instanceof Boolean) {
							el = CheckboxInput.build(name).setDescription(description);
							if (fieldValue != null) {
								el.setPreSetValue(fieldValue.toString());
							}
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
							if (fieldValue != null) {
								((NumberInput)el).presetIntValue(intVal);
							}
						} else {
							el = TextInput.build(name).setDescription(description);
							if (fieldValue != null) {
								el.setPreSetValue(fieldValue.toString());
							}

						}
						this.add(el);

					}

				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// first, fill bean with result of form!
				try {
					BeanUtils.fillBean(this, o);
					addFormValidator(new BeanValidationFormValidator(o));
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};
		// Run hook if bean wants it to tweak additional things
		if (o instanceof FormCheckerBean) {
			((FormCheckerBean)o).preRun(f);
		}
		return f;
	}

	public static void fillBean(FormCheckerForm form, Object bean)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<FormCheckerElement> elements = form.getElements();
		
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
				} else if (propertyVal instanceof Integer) {
					// TBD: We need here try/catch??!
					PropertyUtils.setSimpleProperty(bean, key, Integer.parseInt(elem.getValue()));
				} else if (propertyVal instanceof LocalDate) {
					if (elem instanceof DateInputCompound) {
						Date dateVal = ((DateInputCompound) elem).getDateValue();
						LocalDate dateValLocalDate = new java.sql.Date(dateVal.getTime()).toLocalDate();
						PropertyUtils.setSimpleProperty(bean, key, dateValLocalDate);
					}
				}
			} else {
				logger.info("unable to fill property coming from form: " + key);
			}
		}
		// Run hook if bean wants it to get additionalThings out of the form
		if (bean instanceof FormCheckerBean) {
			((FormCheckerBean)bean).postRun(form);
		}
	}


}
