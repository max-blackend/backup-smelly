/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.spring.security.interceptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import org.jsecurity.mgt.SecurityManager;
import org.jsecurity.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class AuthorizationAttributeSourceAdvisorEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor0 = new AuthorizationAttributeSourceAdvisor();
      SecurityManager securityManager0 = authorizationAttributeSourceAdvisor0.getSecurityManager();
      assertNull(securityManager0);
  }

  @Test
  public void test1()  throws Throwable  {
      AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor0 = new AuthorizationAttributeSourceAdvisor();
      authorizationAttributeSourceAdvisor0.setSecurityManager((SecurityManager) null);
      assertEquals(true, authorizationAttributeSourceAdvisor0.isPerInstance());
  }

  @Test
  public void test2()  throws Throwable  {
      AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor0 = new AuthorizationAttributeSourceAdvisor();
      authorizationAttributeSourceAdvisor0.afterPropertiesSet();
      authorizationAttributeSourceAdvisor0.afterPropertiesSet();
      assertEquals(true, authorizationAttributeSourceAdvisor0.isPerInstance());
  }
}
