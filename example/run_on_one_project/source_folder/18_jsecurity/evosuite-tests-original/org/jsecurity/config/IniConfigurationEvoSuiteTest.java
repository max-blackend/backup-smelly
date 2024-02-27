/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.evosuite.Properties.SandboxMode;
import org.evosuite.sandbox.Sandbox;
import org.jsecurity.JSecurityException;
import org.jsecurity.config.ConfigurationException;
import org.jsecurity.config.IniConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class IniConfigurationEvoSuiteTest {

  private static ExecutorService executor; 

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
    org.evosuite.Properties.SANDBOX_MODE = SandboxMode.RECOMMENDED; 
    Sandbox.initializeSecurityManagerForSUT(); 
    executor = Executors.newCachedThreadPool(); 
  } 

  @AfterClass 
  public static void clearEvoSuiteFramework(){ 
    executor.shutdownNow(); 
    Sandbox.resetDefaultSecurityManager(); 
  } 

  @Before 
  public void initTestCase(){ 
    Sandbox.goingToExecuteSUTCode(); 
  } 

  @After 
  public void doneWithTestCase(){ 
    Sandbox.doneWithExecutingSUTCode(); 
  } 


  @Test
  public void test0()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          IniConfiguration iniConfiguration0 = new IniConfiguration();
          Scanner scanner0 = new Scanner("#M\"PL390oh3!sqVpS7B");
          iniConfiguration0.load(scanner0);
          assertEquals(false, scanner0.hasNextFloat());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test1()  throws Throwable  {
      IniConfiguration iniConfiguration0 = null;
      try {
        iniConfiguration0 = new IniConfiguration((String) null);
        fail("Expecting exception: ConfigurationException");
      } catch(ConfigurationException e) {
        /*
         * java.lang.IllegalArgumentException: 'resourcePath' argument cannot be null.
         */
      }
  }

  @Test
  public void test2()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          IniConfiguration iniConfiguration0 = new IniConfiguration();
          iniConfiguration0.init();
          assertNull(iniConfiguration0.getConfigUrl());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test3()  throws Throwable  {
      IniConfiguration iniConfiguration0 = new IniConfiguration();
      iniConfiguration0.setConfigUrl("#M\"PL390oh3!sqVpS7B");
      try {
        iniConfiguration0.init();
        fail("Expecting exception: ConfigurationException");
      } catch(ConfigurationException e) {
        /*
         * JSecurity resource [#M\"PL390oh3!sqVpS7B] specified as a 'configUrl' cannot be found.  If you want to fall back on default configuration specified via the 'config' parameter, then set 'ignoreResourceNotFound' to true.
         */
      }
  }

  @Test
  public void test4()  throws Throwable  {
      IniConfiguration iniConfiguration0 = null;
      try {
        iniConfiguration0 = new IniConfiguration("1z(kPnJ~dpDg", "1z(kPnJ~dpDg");
        fail("Expecting exception: ConfigurationException");
      } catch(ConfigurationException e) {
        /*
         * org.jsecurity.io.ResourceException: Unable to load text resource from the resource path [1z(kPnJ~dpDg]
         */
      }
  }

  @Test
  public void test5()  throws Throwable  {
      IniConfiguration iniConfiguration0 = new IniConfiguration();
      String string0 = iniConfiguration0.getConfigUrl();
      assertNull(string0);
  }

  @Test
  public void test6()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          IniConfiguration iniConfiguration0 = new IniConfiguration();
          byte[] byteArray0 = new byte[27];
          ByteArrayInputStream byteArrayInputStream0 = new ByteArrayInputStream(byteArray0, (int) (byte) (-118), (int) (byte)0);
          BufferedInputStream bufferedInputStream0 = new BufferedInputStream((InputStream) byteArrayInputStream0);
          PushbackInputStream pushbackInputStream0 = new PushbackInputStream((InputStream) bufferedInputStream0);
          iniConfiguration0.load((InputStream) pushbackInputStream0);
          assertNull(iniConfiguration0.getConfigUrl());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test7()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          IniConfiguration iniConfiguration0 = new IniConfiguration();
          iniConfiguration0.setConfig("");
          iniConfiguration0.init();
          assertEquals("", iniConfiguration0.getConfig());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }
}
