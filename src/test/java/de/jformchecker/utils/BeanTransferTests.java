package de.jformchecker.utils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import de.jformchecker.FormCheckerForm;

public class BeanTransferTests {

	
	@Test
	public void testFillEmptyForm() {
		FormCheckerForm form = BeanUtils.fromBean(new TestBean());
		Assert.assertEquals(4, form.getElements().size());
	}
	
	
	@Test
	public void testFillFullForm() {
		TestBean t = new TestBean();
		String exContent = "Example";
		t.setFirstname(exContent);
		FormCheckerForm form = BeanUtils.fromBean(t);
		Assert.assertEquals(exContent, form.getElement("firstname").getValue());
	}
	
	public void testFromForm(){
		FormCheckerForm form = new ExampleFormShort();
		TestBean bean = new TestBean();
		try {
			BeanUtils.fillBean(form, bean);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
