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

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = false, useJEE = true)
public class CookieAttribute_ESTest_test20 extends CookieAttribute_ESTest_scaffolding {
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
}
