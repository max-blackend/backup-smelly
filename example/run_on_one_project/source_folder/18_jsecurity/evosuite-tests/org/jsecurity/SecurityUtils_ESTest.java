/*
 * This file was automatically generated by EvoSuite
 * Thu Sep 29 18:41:30 GMT 2022
 */

package org.jsecurity;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsecurity.SecurityUtils;
import org.jsecurity.mgt.SecurityManager;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class SecurityUtils_ESTest extends SecurityUtils_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      SecurityManager securityManager0 = SecurityUtils.getSecurityManager();
      assertNull(securityManager0);
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      SecurityUtils.setSecurityManager((SecurityManager) null);
  }
}
