/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.web.attr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.jsecurity.web.attr.AbstractWebAttribute;
import org.jsecurity.web.attr.CookieAttribute;
import org.jsecurity.web.attr.RequestParamAttribute;
import org.jsecurity.web.attr.WebAttribute;

@RunWith(EvoSuiteRunner.class)
public class AbstractWebAttributeEvoSuiteTest {


  //Test case number: 0
  /*
   * 9 covered goals:
   * 1 org.jsecurity.web.attr.AbstractWebAttribute.retrieveValue(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljava/lang/Object;: I8 Branch 7 IFEQ L202 - true
   * 2 org.jsecurity.web.attr.AbstractWebAttribute.retrieveValue(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljava/lang/Object;: I21 Branch 9 IFNONNULL L206 - false
   * 3 org.jsecurity.web.attr.AbstractWebAttribute.setMutable(Z)V: root-Branch
   * 4 org.jsecurity.web.attr.AbstractWebAttribute.setCheckRequestParams(Z)V: root-Branch
   * 5 org.jsecurity.web.attr.AbstractWebAttribute.isCheckRequestParams()Z: root-Branch
   * 6 org.jsecurity.web.attr.AbstractWebAttribute.getName()Ljava/lang/String;: root-Branch
   * 7 org.jsecurity.web.attr.AbstractWebAttribute.setName(Ljava/lang/String;)V: root-Branch
   * 8 org.jsecurity.web.attr.AbstractWebAttribute.<init>(Ljava/lang/String;)V: root-Branch
   * 9 org.jsecurity.web.attr.AbstractWebAttribute.<init>(Ljava/lang/String;Z)V: root-Branch
   */
  @Test
  public void test0()  throws Throwable  {
      RequestParamAttribute<AbstractWebAttribute<PropertyEditor>> requestParamAttribute0 = new RequestParamAttribute<AbstractWebAttribute<PropertyEditor>>("5$RI@-[c[>37");
      // Undeclared exception!
      try {
        requestParamAttribute0.retrieveValue((ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }

  //Test case number: 1
  /*
   * 3 covered goals:
   * 1 org.jsecurity.web.attr.AbstractWebAttribute.<init>(Ljava/lang/String;ZLjava/lang/Class;)V: root-Branch
   * 2 org.jsecurity.web.attr.AbstractWebAttribute.setEditorClass(Ljava/lang/Class;)V: root-Branch
   * 3 org.jsecurity.web.attr.AbstractWebAttribute.<init>(Ljava/lang/String;Ljava/lang/Class;)V: root-Branch
   */
  @Test
  public void test1()  throws Throwable  {
      Class<? extends PropertyEditor> class0 = PropertyEditorSupport.class;
      CookieAttribute<WebAttribute<Object>> cookieAttribute0 = new CookieAttribute<WebAttribute<Object>>("", "", (-16), class0);
      assertEquals("", cookieAttribute0.getName());
      assertEquals(true, cookieAttribute0.isCheckRequestParamsFirst());
      assertEquals(true, cookieAttribute0.isCheckRequestParams());
      assertEquals(true, cookieAttribute0.isMutable());
  }

  //Test case number: 2
  /*
   * 6 covered goals:
   * 1 org.jsecurity.web.attr.AbstractWebAttribute.isCheckRequestParams()Z: root-Branch
   * 2 org.jsecurity.web.attr.AbstractWebAttribute.getName()Ljava/lang/String;: root-Branch
   * 3 org.jsecurity.web.attr.AbstractWebAttribute.<init>()V: root-Branch
   * 4 org.jsecurity.web.attr.AbstractWebAttribute.isCheckRequestParamsFirst()Z: root-Branch
   * 5 org.jsecurity.web.attr.AbstractWebAttribute.retrieveValue(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljava/lang/Object;: I8 Branch 7 IFEQ L202 - false
   * 6 org.jsecurity.web.attr.AbstractWebAttribute.retrieveValue(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljava/lang/Object;: I11 Branch 8 IFEQ L202 - false
   */
  @Test
  public void test2()  throws Throwable  {
      CookieAttribute<WebAttribute<Object>> cookieAttribute0 = new CookieAttribute<WebAttribute<Object>>();
      // Undeclared exception!
      try {
        cookieAttribute0.retrieveValue((ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }

  //Test case number: 3
  /*
   * 6 covered goals:
   * 1 org.jsecurity.web.attr.AbstractWebAttribute.isMutable()Z: root-Branch
   * 2 org.jsecurity.web.attr.AbstractWebAttribute.getEditorClass()Ljava/lang/Class;: root-Branch
   * 3 org.jsecurity.web.attr.AbstractWebAttribute.toStringValue(Ljava/lang/Object;)Ljava/lang/String;: I8 Branch 2 IFNONNULL L167 - false
   * 4 org.jsecurity.web.attr.AbstractWebAttribute.toStringValue(Ljava/lang/Object;)Ljava/lang/String;: I13 Branch 3 IFEQ L169 - false
   * 5 org.jsecurity.web.attr.AbstractWebAttribute.storeValue(Ljava/lang/Object;Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V: I3 Branch 13 IFNONNULL L222 - true
   * 6 org.jsecurity.web.attr.AbstractWebAttribute.storeValue(Ljava/lang/Object;Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V: I20 Branch 15 IFNE L227 - true
   */
  @Test
  public void test3()  throws Throwable  {
      CookieAttribute<AbstractWebAttribute<PropertyEditor>> cookieAttribute0 = new CookieAttribute<AbstractWebAttribute<PropertyEditor>>("=n?4#6Fbs%<S3ef", "=n?4#6Fbs%<S3ef");
      CookieAttribute<PropertyEditor> cookieAttribute1 = new CookieAttribute<PropertyEditor>("=n?4#6Fbs%<S3ef", (-10));
      // Undeclared exception!
      try {
        cookieAttribute0.storeValue((AbstractWebAttribute<PropertyEditor>) cookieAttribute1, (ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }

  //Test case number: 4
  /*
   * 6 covered goals:
   * 1 org.jsecurity.web.attr.AbstractWebAttribute.retrieveValue(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljava/lang/Object;: I11 Branch 8 IFEQ L202 - true
   * 2 org.jsecurity.web.attr.AbstractWebAttribute.isCheckRequestParams()Z: root-Branch
   * 3 org.jsecurity.web.attr.AbstractWebAttribute.setCheckRequestParamsFirst(Z)V: root-Branch
   * 4 org.jsecurity.web.attr.AbstractWebAttribute.isCheckRequestParamsFirst()Z: root-Branch
   * 5 org.jsecurity.web.attr.AbstractWebAttribute.retrieveValue(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljava/lang/Object;: I8 Branch 7 IFEQ L202 - false
   * 6 org.jsecurity.web.attr.AbstractWebAttribute.retrieveValue(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)Ljava/lang/Object;: I21 Branch 9 IFNONNULL L206 - false
   */
  @Test
  public void test4()  throws Throwable  {
      CookieAttribute<WebAttribute<Object>> cookieAttribute0 = new CookieAttribute<WebAttribute<Object>>();
      assertEquals(true, cookieAttribute0.isCheckRequestParamsFirst());
      
      cookieAttribute0.setCheckRequestParamsFirst(false);
      // Undeclared exception!
      try {
        cookieAttribute0.retrieveValue((ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }

  //Test case number: 5
  /*
   * 3 covered goals:
   * 1 org.jsecurity.web.attr.AbstractWebAttribute.storeValue(Ljava/lang/Object;Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V: I3 Branch 13 IFNONNULL L222 - false
   * 2 org.jsecurity.web.attr.AbstractWebAttribute.storeValue(Ljava/lang/Object;Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V: I6 Branch 14 IFEQ L222 - false
   * 3 org.jsecurity.web.attr.AbstractWebAttribute.<init>(Ljava/lang/String;)V: root-Branch
   */
  @Test
  public void test5()  throws Throwable  {
      CookieAttribute<PropertyEditor> cookieAttribute0 = new CookieAttribute<PropertyEditor>(">Y~9P4#6!N*kO3^");
      // Undeclared exception!
      try {
        cookieAttribute0.storeValue((PropertyEditor) null, (ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }

  //Test case number: 6
  /*
   * 10 covered goals:
   * 1 org.jsecurity.web.attr.AbstractWebAttribute.storeValue(Ljava/lang/Object;Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V: I6 Branch 14 IFEQ L222 - true
   * 2 org.jsecurity.web.attr.AbstractWebAttribute.storeValue(Ljava/lang/Object;Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V: I20 Branch 15 IFNE L227 - false
   * 3 org.jsecurity.web.attr.AbstractWebAttribute.setMutable(Z)V: root-Branch
   * 4 org.jsecurity.web.attr.AbstractWebAttribute.setCheckRequestParams(Z)V: root-Branch
   * 5 org.jsecurity.web.attr.AbstractWebAttribute.isMutable()Z: root-Branch
   * 6 org.jsecurity.web.attr.AbstractWebAttribute.getName()Ljava/lang/String;: root-Branch
   * 7 org.jsecurity.web.attr.AbstractWebAttribute.setName(Ljava/lang/String;)V: root-Branch
   * 8 org.jsecurity.web.attr.AbstractWebAttribute.<init>()V: root-Branch
   * 9 org.jsecurity.web.attr.AbstractWebAttribute.<init>(Ljava/lang/String;Z)V: root-Branch
   * 10 org.jsecurity.web.attr.AbstractWebAttribute.storeValue(Ljava/lang/Object;Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;)V: I3 Branch 13 IFNONNULL L222 - false
   */
  @Test
  public void test6()  throws Throwable  {
      RequestParamAttribute<PropertyEditor> requestParamAttribute0 = new RequestParamAttribute<PropertyEditor>();
      // Undeclared exception!
      try {
        requestParamAttribute0.storeValue((PropertyEditor) null, (ServletRequest) null, (ServletResponse) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }
}
