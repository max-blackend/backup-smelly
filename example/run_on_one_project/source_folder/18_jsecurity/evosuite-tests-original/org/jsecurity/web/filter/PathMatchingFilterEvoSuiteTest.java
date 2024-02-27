/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.web.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.jsecurity.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jsecurity.web.filter.authz.RolesAuthorizationFilter;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class PathMatchingFilterEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      RolesAuthorizationFilter rolesAuthorizationFilter0 = new RolesAuthorizationFilter();
      rolesAuthorizationFilter0.processPathConfig(".(m`+ZpYbH$uz", (String) null);
      try {
        rolesAuthorizationFilter0.preHandle((ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }

  @Test
  public void test1()  throws Throwable  {
      RolesAuthorizationFilter rolesAuthorizationFilter0 = new RolesAuthorizationFilter();
      boolean boolean0 = rolesAuthorizationFilter0.pathsMatch("yl99aE-}QvJ", "yl99aE-}QvJ");
      assertEquals(true, boolean0);
  }

  @Test
  public void test2()  throws Throwable  {
      RolesAuthorizationFilter rolesAuthorizationFilter0 = new RolesAuthorizationFilter();
      rolesAuthorizationFilter0.processPathConfig("yl99aE-}QvJ", "yl99aE-}QvJ");
  }

  @Test
  public void test3()  throws Throwable  {
      BasicHttpAuthenticationFilter basicHttpAuthenticationFilter0 = new BasicHttpAuthenticationFilter();
      boolean boolean0 = basicHttpAuthenticationFilter0.preHandle((ServletRequest) null, (ServletResponse) null);
      assertEquals(true, boolean0);
  }
}