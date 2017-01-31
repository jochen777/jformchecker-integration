package de.jformchecker.utils;

import org.junit.Assert;
import org.junit.Test;

import de.jformchecker.FormChecker;
import de.jformchecker.FormCheckerForm;

/**
 * Tests for Forms, that are created from Beans with Validation
 * @author jochen
 *
 */
public class TestBeanValidation {

	@Test
	public void testFormCheckerWithValidation() {
		BeanWithValidation bean = new BeanWithValidation();
		FormCheckerForm form = BeanUtils.fromBean(bean);
		FormChecker fc = RequestBuilders.buildFcWithEmptyRequest();
		fc.addForm(form);
		fc.run();
		Assert.assertTrue(fc.isValid());
	}
}
