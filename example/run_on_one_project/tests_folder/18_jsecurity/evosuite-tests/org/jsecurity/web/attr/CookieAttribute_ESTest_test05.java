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
public class CookieAttribute_ESTest_test05 extends CookieAttribute_ESTest_scaffolding {
  @Test(timeout = 4000)
    public void test05()  throws Throwable  {
        CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("", "", (-2545));
        int int0 = cookieAttribute0.getMaxAge();
        assertFalse(cookieAttribute0.isSecure());
        assertEquals((-2545), int0);
        assertEquals("", cookieAttribute0.getPath());
    }
}
