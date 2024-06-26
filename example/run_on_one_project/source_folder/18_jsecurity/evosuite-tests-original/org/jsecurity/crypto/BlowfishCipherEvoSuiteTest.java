/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.crypto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.jsecurity.crypto.BlowfishCipher;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class BlowfishCipherEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      BlowfishCipher blowfishCipher0 = new BlowfishCipher();
      byte[] byteArray0 = new byte[1];
      Cipher cipher0 = blowfishCipher0.newCipherInstance();
      // Undeclared exception!
      try {
        blowfishCipher0.crypt(cipher0, byteArray0);
        fail("Expecting exception: IllegalStateException");
      } catch(IllegalStateException e) {
        /*
         * Unable to crypt bytes with cipher [javax.crypto.Cipher@29d12e9d].
         */
      }
  }

  @Test
  public void test1()  throws Throwable  {
      BlowfishCipher blowfishCipher0 = new BlowfishCipher();
      // Undeclared exception!
      try {
        blowfishCipher0.encrypt((byte[]) null, (byte[]) null);
        fail("Expecting exception: NoClassDefFoundError");
      } catch(NoClassDefFoundError e) {
        /*
         * Could not initialize class javax.crypto.SunJCE_h
         */
      }
  }

  @Test
  public void test2()  throws Throwable  {
      BlowfishCipher blowfishCipher0 = new BlowfishCipher();
      SecretKeySpec secretKeySpec0 = (SecretKeySpec)BlowfishCipher.generateNewKey();
      blowfishCipher0.setKey((Key) secretKeySpec0);
      assertEquals("RAW", secretKeySpec0.getFormat());
  }

  @Test
  public void test3()  throws Throwable  {
      byte[] byteArray0 = new byte[1];
      BlowfishCipher blowfishCipher0 = new BlowfishCipher();
      // Undeclared exception!
      try {
        blowfishCipher0.decrypt(byteArray0, byteArray0);
        fail("Expecting exception: NoClassDefFoundError");
      } catch(NoClassDefFoundError e) {
        /*
         * Could not initialize class javax.crypto.SunJCE_h
         */
      }
  }
}
