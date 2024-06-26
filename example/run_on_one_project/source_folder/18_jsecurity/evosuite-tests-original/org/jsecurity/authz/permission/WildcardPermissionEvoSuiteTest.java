/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.authz.permission;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import org.jsecurity.authz.Permission;
import org.jsecurity.authz.permission.AllPermission;
import org.jsecurity.authz.permission.WildcardPermission;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class WildcardPermissionEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      WildcardPermission wildcardPermission0 = null;
      try {
        wildcardPermission0 = new WildcardPermission((String) null);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
        /*
         * Wildcard string cannot be null or empty. Make sure permission strings are properly formatted.
         */
      }
  }

  @Test
  public void test1()  throws Throwable  {
      WildcardPermission wildcardPermission0 = null;
      try {
        wildcardPermission0 = new WildcardPermission("");
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
        /*
         * Wildcard string cannot be null or empty. Make sure permission strings are properly formatted.
         */
      }
  }

  @Test
  public void test2()  throws Throwable  {
      WildcardPermission wildcardPermission0 = null;
      try {
        wildcardPermission0 = new WildcardPermission(",", true);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
        /*
         * Wildcard string cannot contain parts with only dividers. Make sure permission strings are properly formatted.
         */
      }
  }

  @Test
  public void test3()  throws Throwable  {
      WildcardPermission wildcardPermission0 = null;
      try {
        wildcardPermission0 = new WildcardPermission(":");
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
        /*
         * Wildcard string cannot contain only dividers. Make sure permission strings are properly formatted.
         */
      }
  }

  @Test
  public void test4()  throws Throwable  {
      WildcardPermission wildcardPermission0 = new WildcardPermission("*");
      assertNotNull(wildcardPermission0);
      
      AllPermission allPermission0 = new AllPermission();
      boolean boolean0 = wildcardPermission0.implies((Permission) allPermission0);
      assertEquals(false, boolean0);
  }

  @Test
  public void test5()  throws Throwable  {
      WildcardPermission wildcardPermission0 = new WildcardPermission("*");
      assertNotNull(wildcardPermission0);
      
      WildcardPermission wildcardPermission1 = new WildcardPermission("*:1H_");
      boolean boolean0 = wildcardPermission0.implies((Permission) wildcardPermission1);
      assertEquals(true, boolean0);
  }

  @Test
  public void test6()  throws Throwable  {
      WildcardPermission wildcardPermission0 = new WildcardPermission("*");
      assertNotNull(wildcardPermission0);
      
      WildcardPermission wildcardPermission1 = new WildcardPermission(" -x@n%V");
      boolean boolean0 = wildcardPermission1.implies((Permission) wildcardPermission0);
      assertEquals(false, boolean0);
  }

  @Test
  public void test7()  throws Throwable  {
      WildcardPermission wildcardPermission0 = new WildcardPermission("<d+S)nB^2");
      assertNotNull(wildcardPermission0);
      
      boolean boolean0 = wildcardPermission0.implies((Permission) wildcardPermission0);
      assertEquals(true, boolean0);
  }

  @Test
  public void test8()  throws Throwable  {
      WildcardPermission wildcardPermission0 = new WildcardPermission("*");
      assertNotNull(wildcardPermission0);
      
      WildcardPermission wildcardPermission1 = new WildcardPermission("*:1H_");
      boolean boolean0 = wildcardPermission1.implies((Permission) wildcardPermission0);
      assertEquals(false, boolean0);
  }
}
