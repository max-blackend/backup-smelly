/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import org.jsecurity.spring.SpringJSecurityFilter;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class SpringJSecurityFilterEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      SpringJSecurityFilter springJSecurityFilter0 = new SpringJSecurityFilter();
      assertNotNull(springJSecurityFilter0);
  }
}
