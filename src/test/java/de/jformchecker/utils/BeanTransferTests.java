package de.jformchecker.utils;

import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import de.jformchecker.FormCheckerElement;
import de.jformchecker.FormCheckerForm;

public class BeanTransferTests {

  String exContent = "Example";


  @Test
  public void testFillEmptyForm() {
    FormCheckerForm form = BeanUtils.fromBean(new TestBean());
    Assert.assertEquals(5, form.getElements().size());
  }


  @Test
  public void testFillPrefilledForm() {
    TestBean t = new TestBean();
    t.setFirstname(exContent);
    FormCheckerForm form = BeanUtils.fromBean(t);
    Assert.assertEquals(exContent, form.getElement("firstname").getValue());
  }

  @Test
  public void testFromForm() {
    FormCheckerForm form = new ExampleFormShort();
    FormCheckerElement elem = form.getElement("firstname");
    elem.setValue(exContent);

    TestBean bean = new TestBean();
    try {
      System.err.println(Utils.getDebugOutput(form));
      BeanUtils.fillBean(form, bean);
      System.err.println(bean);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Assert.assertEquals(exContent, bean.getFirstname());

  }



}
