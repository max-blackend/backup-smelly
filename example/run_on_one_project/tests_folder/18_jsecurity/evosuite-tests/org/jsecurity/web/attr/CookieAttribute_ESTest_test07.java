package org.jsecurity.web.attr;;

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
public class CookieAttribute_ESTest_test07 extends CookieAttribute_ESTest_scaffolding {
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
}
