/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.authz.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import org.jsecurity.authz.UnauthenticatedException;
import org.jsecurity.authz.aop.AuthenticatedAnnotationHandler;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class AuthenticatedAnnotationHandlerEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      AuthenticatedAnnotationHandler authenticatedAnnotationHandler0 = new AuthenticatedAnnotationHandler();
      authenticatedAnnotationHandler0.assertAuthorized((Annotation) null);
  }
}
