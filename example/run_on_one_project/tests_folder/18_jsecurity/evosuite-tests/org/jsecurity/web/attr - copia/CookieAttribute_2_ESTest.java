/*
 * This file was automatically generated by EvoSuite
 * Thu May 07 09:05:28 GMT 2020
 */

package org.jsecurity.web.attr;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.beans.PropertyEditorSupport;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jsecurity.web.attr.CookieAttribute;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true) 
public class CookieAttribute_ESTest extends CookieAttribute_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      CookieAttribute<PropertyEditorSupport> cookieAttribute0 = new CookieAttribute<PropertyEditorSupport>("\" was loaded by ", "\" was loaded by ");
      cookieAttribute0.setSecure(false);
      assertFalse(cookieAttribute0.isSecure());
      assertEquals("\" was loaded by ", cookieAttribute0.getPath());
      assertEquals((-1), cookieAttribute0.getMaxAge());
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("", "", (-1859));
      assertFalse(cookieAttribute0.isSecure());
      
      cookieAttribute0.setSecure(true);
      boolean boolean0 = cookieAttribute0.isSecure();
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      Class<PropertyEditorSupport> class0 = PropertyEditorSupport.class;
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("", (String) null, Integer.MAX_VALUE, class0);
      String string0 = cookieAttribute0.getPath();
      assertEquals(Integer.MAX_VALUE, cookieAttribute0.getMaxAge());
      assertNull(string0);
      assertFalse(cookieAttribute0.isSecure());
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      CookieAttribute<String> cookieAttribute0 = new CookieAttribute<String>("", "");
      String string0 = cookieAttribute0.getPath();
      assertFalse(cookieAttribute0.isSecure());
      assertEquals("", string0);
      assertEquals((-1), cookieAttribute0.getMaxAge());
      assertNotNull(string0);
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      CookieAttribute<Object> cookieAttribute0 = new CookieAttribute<Object>("u]n,IA6", "u]n,IA6", 0);
      int int0 = cookieAttribute0.getMaxAge();
      assertEquals(0, int0);
      assertFalse(cookieAttribute0.isSecure());
      assertEquals("u]n,IA6", cookieAttribute0.getPath());
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("", "", (-2545));
      int int0 = cookieAttribute0.getMaxAge();
      assertFalse(cookieAttribute0.isSecure());
      assertEquals((-2545), int0);
      assertEquals("", cookieAttribute0.getPath());
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      CookieAttribute<PropertyEditorSupport> cookieAttribute0 = new CookieAttribute<PropertyEditorSupport>();
      // Undeclared exception!
      try { 
        cookieAttribute0.removeValue((ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.jsecurity.web.attr.CookieAttribute", e);
      }
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      CookieAttribute<String> cookieAttribute0 = new CookieAttribute<String>();
      ServletRequest servletRequest0 = mock(ServletRequest.class, new ViolatedAssumptionAnswer());
      ServletRequestWrapper servletRequestWrapper0 = new ServletRequestWrapper(servletRequest0);
      // Undeclared exception!
      try { 
        cookieAttribute0.removeValue(servletRequestWrapper0, (ServletResponse) null);
        fail("Expecting exception: ClassCastException");
      
      } catch(ClassCastException e) {
         //
         // javax.servlet.ServletRequestWrapper cannot be cast to javax.servlet.http.HttpServletRequest
         //
         verifyException("org.jsecurity.web.WebUtils", e);
      }
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      CookieAttribute<String> cookieAttribute0 = new CookieAttribute<String>(",5[W!", "name");
      // Undeclared exception!
      try { 
        cookieAttribute0.onStoreValue(",5[W!", (ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: IllegalArgumentException");
      
      } catch(IllegalArgumentException e) {
         //
         // Cookie name \",5[W!\" is a reserved token
         //
         verifyException("javax.servlet.http.Cookie", e);
      }
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      ServletResponse servletResponse0 = mock(ServletResponse.class, new ViolatedAssumptionAnswer());
      CookieAttribute<String> cookieAttribute0 = new CookieAttribute<String>();
      // Undeclared exception!
      try { 
        cookieAttribute0.onStoreValue("name", (ServletRequest) null, servletResponse0);
        fail("Expecting exception: ClassCastException");
      
      } catch(ClassCastException e) {
         //
         // codegen.javax.servlet.ServletResponse$MockitoMock$1012536142 cannot be cast to javax.servlet.http.HttpServletResponse
         //
         verifyException("org.jsecurity.web.WebUtils", e);
      }
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      ServletResponse servletResponse0 = mock(ServletResponse.class, new ViolatedAssumptionAnswer());
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>();
      ServletRequest servletRequest0 = mock(ServletRequest.class, new ViolatedAssumptionAnswer());
      ServletRequestWrapper servletRequestWrapper0 = new ServletRequestWrapper(servletRequest0);
      // Undeclared exception!
      try { 
        cookieAttribute0.onRetrieveValue(servletRequestWrapper0, servletResponse0);
        fail("Expecting exception: ClassCastException");
      
      } catch(ClassCastException e) {
         //
         // javax.servlet.ServletRequestWrapper cannot be cast to javax.servlet.http.HttpServletRequest
         //
         verifyException("org.jsecurity.web.WebUtils", e);
      }
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      Class<PropertyEditorSupport> class0 = PropertyEditorSupport.class;
      CookieAttribute<Object> cookieAttribute0 = new CookieAttribute<Object>("name", "9_,8hhG;l", 31536000, class0);
      int int0 = cookieAttribute0.getMaxAge();
      assertFalse(cookieAttribute0.isSecure());
      assertEquals(31536000, int0);
      assertEquals("9_,8hhG;l", cookieAttribute0.getPath());
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      Class<PropertyEditorSupport> class0 = PropertyEditorSupport.class;
      CookieAttribute<Object> cookieAttribute0 = new CookieAttribute<Object>("z@B*D~yP[q", "@@c", 40000, class0);
      String string0 = cookieAttribute0.getPath();
      assertEquals(40000, cookieAttribute0.getMaxAge());
      assertEquals("@@c", string0);
      assertNotNull(string0);
      assertFalse(cookieAttribute0.isSecure());
  }

  @Test(timeout = 4000)
  public void test13()  throws Throwable  {
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("", "", (-1859));
      boolean boolean0 = cookieAttribute0.isSecure();
      assertFalse(boolean0);
      assertEquals((-1859), cookieAttribute0.getMaxAge());
      assertEquals("", cookieAttribute0.getPath());
  }

  @Test(timeout = 4000)
  public void test14()  throws Throwable  {
      Class<PropertyEditorSupport> class0 = PropertyEditorSupport.class;
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("name", "log4j.reset", (-1), class0);
      assertEquals("log4j.reset", cookieAttribute0.getPath());
      
      cookieAttribute0.setPath("Gp9,\"?C[");
      assertFalse(cookieAttribute0.isSecure());
  }

  @Test(timeout = 4000)
  public void test15()  throws Throwable  {
      Class<PropertyEditorSupport> class0 = PropertyEditorSupport.class;
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("name", "log4j.reset", (-1), class0);
      cookieAttribute0.setMaxAge((-3410));
      assertEquals((-3410), cookieAttribute0.getMaxAge());
  }

  @Test(timeout = 4000)
  public void test16()  throws Throwable  {
      CookieAttribute<Object> cookieAttribute0 = new CookieAttribute<Object>();
      // Undeclared exception!
      try { 
        cookieAttribute0.onStoreValue((Object) null, (ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.jsecurity.web.attr.CookieAttribute", e);
      }
  }

  @Test(timeout = 4000)
  public void test17()  throws Throwable  {
      CookieAttribute<PropertyEditorSupport> cookieAttribute0 = new CookieAttribute<PropertyEditorSupport>((String) null, Integer.MAX_VALUE);
      assertFalse(cookieAttribute0.isSecure());
      assertEquals(Integer.MAX_VALUE, cookieAttribute0.getMaxAge());
  }

  @Test(timeout = 4000)
  public void test18()  throws Throwable  {
      CookieAttribute<String> cookieAttribute0 = new CookieAttribute<String>("name", "]", 31535988);
      cookieAttribute0.setSecure(true);
      // Undeclared exception!
      try { 
        cookieAttribute0.onStoreValue("ALlV$~N$ae(xZvB$#:", (ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.jsecurity.web.attr.CookieAttribute", e);
      }
  }

  @Test(timeout = 4000)
  public void test19()  throws Throwable  {
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>();
      // Undeclared exception!
      try { 
        cookieAttribute0.onRetrieveValue((ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.jsecurity.web.attr.CookieAttribute", e);
      }
  }

  @Test(timeout = 4000)
  public void test20()  throws Throwable  {
      CookieAttribute<String> cookieAttribute0 = new CookieAttribute<String>("name", "name", Integer.MAX_VALUE);
      // Undeclared exception!
      try { 
        cookieAttribute0.onStoreValue("name", (ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("org.jsecurity.web.attr.CookieAttribute", e);
      }
  }

  @Test(timeout = 4000)
  public void test21()  throws Throwable  {
      CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("name");
      assertEquals((-1), cookieAttribute0.getMaxAge());
      assertFalse(cookieAttribute0.isSecure());
  }
}
