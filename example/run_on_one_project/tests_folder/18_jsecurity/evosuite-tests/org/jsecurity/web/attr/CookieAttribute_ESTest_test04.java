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
public class CookieAttribute_ESTest_test04 extends CookieAttribute_ESTest_scaffolding {
  @Test(timeout = 4000)
    public void test04()  throws Throwable  {
        CookieAttribute<Object> cookieAttribute0 = new CookieAttribute<Object>("u]n,IA6", "u]n,IA6", 0);
        int int0 = cookieAttribute0.getMaxAge();
        assertEquals(0, int0);
        assertFalse(cookieAttribute0.isSecure());
        assertEquals("u]n,IA6", cookieAttribute0.getPath());
    }
}
