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
public class CookieAttribute_ESTest_test11 extends CookieAttribute_ESTest_scaffolding {
  @Test(timeout = 4000)
    public void test11()  throws Throwable  {
        Class<PropertyEditorSupport> class0 = PropertyEditorSupport.class;
        CookieAttribute<Object> cookieAttribute0 = new CookieAttribute<Object>("name", "9_,8hhG;l", 31536000, class0);
        int int0 = cookieAttribute0.getMaxAge();
        assertFalse(cookieAttribute0.isSecure());
        assertEquals(31536000, int0);
        assertEquals("9_,8hhG;l", cookieAttribute0.getPath());
    }
}
