/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.web.tags;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import javax.servlet.jsp.JspException;
import org.jsecurity.web.tags.LacksPermissionTag;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class LacksPermissionTagEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      LacksPermissionTag lacksPermissionTag0 = new LacksPermissionTag();
      int int0 = lacksPermissionTag0.onDoStartTag();
      assertEquals(1, int0);
  }
}
