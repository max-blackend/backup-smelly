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
public class CookieAttribute_ESTest_test10 extends CookieAttribute_ESTest_scaffolding {
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
}
