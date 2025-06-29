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
public class CookieAttribute_ESTest_test14 extends CookieAttribute_ESTest_scaffolding {
  @Test(timeout = 4000)
    public void test14()  throws Throwable  {
        Class<PropertyEditorSupport> class0 = PropertyEditorSupport.class;
        CookieAttribute<Integer> cookieAttribute0 = new CookieAttribute<Integer>("name", "log4j.reset", (-1), class0);
        assertEquals("log4j.reset", cookieAttribute0.getPath());

        cookieAttribute0.setPath("Gp9,\"?C[");
        assertFalse(cookieAttribute0.isSecure());
    }
}
