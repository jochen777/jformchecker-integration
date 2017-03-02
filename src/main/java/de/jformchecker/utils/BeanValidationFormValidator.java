package de.jformchecker.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import de.jformchecker.FormCheckerForm;
import de.jformchecker.FormValidator;
import de.jformchecker.criteria.ValidationResult;

/**
 * Form Validtor for Bean-Validation usage
 * 
 * @author jochen
 *
 */
public class BeanValidationFormValidator implements FormValidator {

	Object bean;

	public BeanValidationFormValidator(Object o) {
		bean = o;
	}

	@Override
	public boolean validate(FormCheckerForm form) {
		try {
			BeanUtils.fillBean(form, bean);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Move to more central place! (or get it from outside)
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);
		System.err.println("JOchen:" + Utils.getDebugOutput(form));
		boolean formIsOkay = false;
		if (constraintViolations.isEmpty()) {
			formIsOkay = true;
		}
		constraintViolations.forEach(violation -> {
			System.err.println("LL:" + violation.getPropertyPath().toString());
			System.err.println(form.getElement(violation.getPropertyPath().toString()));
			form.getElement(violation.getPropertyPath().toString())
					.setValidationResult(ValidationResult.failWithTranslated(violation.getMessage()));
		});
		constraintViolations.forEach(violation -> System.err.println("::" + violation.getPropertyPath().toString()));
		return formIsOkay;
	}

}
