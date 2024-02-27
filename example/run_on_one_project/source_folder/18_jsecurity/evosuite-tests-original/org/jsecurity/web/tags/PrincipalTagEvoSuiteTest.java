/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.web.tags;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import javax.servlet.jsp.JspException;
import org.jsecurity.web.tags.PrincipalTag;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class PrincipalTagEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      PrincipalTag principalTag0 = new PrincipalTag();
      principalTag0.setType((String) null);
      assertEquals(0, principalTag0.doAfterBody());
  }

  @Test
  public void test1()  throws Throwable  {
      PrincipalTag principalTag0 = new PrincipalTag();
      principalTag0.setProperty((String) null);
      assertEquals(6, principalTag0.doEndTag());
  }

  @Test
  public void test2()  throws Throwable  {
      PrincipalTag principalTag0 = new PrincipalTag();
      String string0 = principalTag0.getProperty();
      assertNull(string0);
  }

  @Test
  public void test3()  throws Throwable  {
      PrincipalTag principalTag0 = new PrincipalTag();
      String string0 = principalTag0.getDefaultValue();
      assertNull(string0);
  }

  @Test
  public void test4()  throws Throwable  {
      PrincipalTag principalTag0 = new PrincipalTag();
      principalTag0.setDefaultValue((String) null);
      assertNull(principalTag0.getType());
  }

  @Test
  public void test5()  throws Throwable  {
      PrincipalTag principalTag0 = new PrincipalTag();
      String string0 = principalTag0.getType();
      assertNull(string0);
  }

  @Test
  public void test6()  throws Throwable  {
      PrincipalTag principalTag0 = new PrincipalTag();
      int int0 = principalTag0.onDoStartTag();
      assertEquals(0, int0);
  }
}
