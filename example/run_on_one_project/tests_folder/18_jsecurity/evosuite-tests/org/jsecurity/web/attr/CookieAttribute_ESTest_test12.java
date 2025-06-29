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
public class CookieAttribute_ESTest_test12 extends CookieAttribute_ESTest_scaffolding {
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
}
