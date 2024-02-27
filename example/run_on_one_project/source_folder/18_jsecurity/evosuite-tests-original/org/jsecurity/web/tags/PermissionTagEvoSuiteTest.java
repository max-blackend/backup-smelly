/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.web.tags;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import javax.servlet.jsp.JspException;
import org.jsecurity.web.tags.HasPermissionTag;
import org.jsecurity.web.tags.LacksPermissionTag;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class PermissionTagEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      HasPermissionTag hasPermissionTag0 = new HasPermissionTag();
      try {
        hasPermissionTag0.doStartTag();
        fail("Expecting exception: JspException");
      } catch(JspException e) {
        /*
         * The 'name' tag attribute must be set.
         */
      }
  }

  @Test
  public void test1()  throws Throwable  {
      HasPermissionTag hasPermissionTag0 = new HasPermissionTag();
      hasPermissionTag0.setName("");
      try {
        hasPermissionTag0.doStartTag();
        fail("Expecting exception: JspException");
      } catch(JspException e) {
        /*
         * The 'name' tag attribute must be set.
         */
      }
  }

  @Test
  public void test2()  throws Throwable  {
      HasPermissionTag hasPermissionTag0 = new HasPermissionTag();
      hasPermissionTag0.setName("giag%py:abt_c2");
      int int0 = hasPermissionTag0.doStartTag();
      assertEquals(0, int0);
  }

  @Test
  public void test3()  throws Throwable  {
      LacksPermissionTag lacksPermissionTag0 = new LacksPermissionTag();
      int int0 = lacksPermissionTag0.onDoStartTag();
      assertEquals(1, int0);
  }
}
