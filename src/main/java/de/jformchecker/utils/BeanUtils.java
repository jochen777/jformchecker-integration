package de.jformchecker.utils;

import java.beans.PropertyDescriptor;
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
import de.jformchecker.elements.LongTextInput;
import de.jformchecker.elements.NumberInput;
import de.jformchecker.elements.TextInput;
import de.jformchecker.fieldmarkers.FieldType;
import de.jformchecker.fieldmarkers.LongText;

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

					for (Field f : o.getClass().getDeclaredFields()) {	// TODO: Either check the fields (which is better i think) or read bean-methods. Now it is mixed
						// check if it is ignored
						IgnoreFormElement ignored = f.getAnnotation(IgnoreFormElement.class);
						if (ignored != null) {
							continue;
						}

						String name = f.getName();
						Object fieldValue = null;
						try {
						fieldValue = PropertyUtils.getProperty(o, f.getName());
						} catch (InvocationTargetException e) {
							// field value is null, no problem
						}
						String description = name;
						Label label = f.getAnnotation(Label.class);
						if (label != null) {
							description = label.text();
						}
						FormCheckerElement el;

						LongText longTextMarked = f.getAnnotation(LongText.class);
						if (longTextMarked != null) {
							el = LongTextInput.build(name).setDescription(description);
							if (fieldValue != null) {
								el.setPreSetValue(fieldValue.toString());
							}
						} else
						if (f.getAnnotation(FieldType.class) != null) {
							FieldType fieldType = f.getAnnotation(FieldType.class);
							Class<? extends FormCheckerElement> c =  fieldType.type();
								FormCheckerElement i = c.newInstance();
								i.setDescription(description);
								i.setName(name);
								el = i;
								
							
						}
						else if (fieldValue instanceof Boolean) {
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
								((NumberInput) el).presetIntValue(intVal);
							}
						} else {
							el = TextInput.build(name).setDescription(description);
							if (fieldValue != null) {
								el.setPreSetValue(fieldValue.toString());
							}

						}
						this.add(el);

					}

				} catch (IllegalAccessException  | NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
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
			((FormCheckerBean) o).preRun(f);
		}
		return f;
	}

	public static void fillBean(FormCheckerForm form, Object bean)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<FormCheckerElement> elements = form.getElements();

		for (FormCheckerElement elem : elements) {
			String key = elem.getName();
			if (PropertyUtils.isWriteable(bean, key)) {
				PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, key);

				Object className = pd.getPropertyType().getName();
				if (className.equals("java.lang.String")) {
					PropertyUtils.setSimpleProperty(bean, key, elem.getValue());
				} else if (className.equals("java.lang.Boolean") || className.equals("boolean")) {
					if ("true".equals(elem.getValue())) {
						PropertyUtils.setSimpleProperty(bean, key, true);
					} else {
						PropertyUtils.setSimpleProperty(bean, key, false);
					}
				} else if (className.equals("java.lang.Integer") || className.equals("int")) {
					try {
						if (elem.getValue() != null && !"".equals(elem.getValue())) {
							PropertyUtils.setSimpleProperty(bean, key, Integer.parseInt(elem.getValue()));
						}
					} catch (NumberFormatException e) {
						logger.info("unable to set int property because can not parse to int", e);
					}
				} else if (className.equals("java.lang.Long") || className.equals("long")) {
					try {
						PropertyUtils.setSimpleProperty(bean, key, Long.parseLong(elem.getValue()));
					} catch (NumberFormatException e) {
						logger.info("unable to set long property because can not parse to int", e);
					}
				} else if (className.equals("java.time.LocalDate")) {
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
			((FormCheckerBean) bean).postRun(form);
		}
	}

}
