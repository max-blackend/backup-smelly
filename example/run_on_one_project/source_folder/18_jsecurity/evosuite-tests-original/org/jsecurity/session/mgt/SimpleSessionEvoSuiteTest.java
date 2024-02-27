/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.session.mgt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.evosuite.Properties.SandboxMode;
import org.evosuite.sandbox.Sandbox;
import org.jsecurity.session.ExpiredSessionException;
import org.jsecurity.session.InvalidSessionException;
import org.jsecurity.session.StoppedSessionException;
import org.jsecurity.session.mgt.SimpleSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class SimpleSessionEvoSuiteTest {

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
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.setAttribute((Object) "localhost", (Object) "localhost");
          simpleSession0.removeAttribute((Object) simpleSession0);
          assertEquals(false, simpleSession0.isTimedOut());
          assertEquals(1800000L, simpleSession0.getTimeout());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test1()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          Date date0 = new Date();
          simpleSession0.setStartTimestamp(date0);
          assertEquals(1372791280281L, date0.getTime());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test2()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.touch();
          assertEquals(true, simpleSession0.isValid());
          assertEquals(1800000L, simpleSession0.getTimeout());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test3()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          Date date0 = simpleSession0.getStartTimestamp();
          assertEquals(1372791280380L, date0.getTime());
          assertNotNull(date0);
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test4()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.setExpired(true);
          try {
            simpleSession0.validate();
            fail("Expecting exception: ExpiredSessionException");
          } catch(ExpiredSessionException e) {
            /*
             * Session with id [null] has expired. Last access time: 02/07/13 19:54.  Current time: 02/07/13 19:54.  Session timeout is set to 1800 seconds (30 minutes)
             */
          }
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test5()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          Date date0 = new Date();
          simpleSession0.setStopTimestamp(date0);
          assertEquals(1372791280573L, date0.getTime());
          assertEquals(1800000L, simpleSession0.getTimeout());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test6()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          Inet4Address inet4Address0 = (Inet4Address)simpleSession0.getHostAddress();
          assertNotNull(inet4Address0);
          
          simpleSession0.setHostAddress((InetAddress) inet4Address0);
          assertEquals(1800000L, simpleSession0.getTimeout());
          assertEquals(false, simpleSession0.isExpired());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test7()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.setTimeout((-1839L));
          simpleSession0.validate();
          assertEquals((-1839L), simpleSession0.getTimeout());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test8()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          Date date0 = new Date();
          simpleSession0.setLastAccessTime(date0);
          assertEquals(1372791280813L, date0.getTime());
          assertEquals(false, simpleSession0.isTimedOut());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test9()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.setId((Serializable) (-1839L));
          assertEquals(1800000L, simpleSession0.getTimeout());
          assertEquals(false, simpleSession0.isTimedOut());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test10()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.expire();
          simpleSession0.expire();
          assertEquals(true, simpleSession0.isStopped());
          assertEquals(1800000L, simpleSession0.getTimeout());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test11()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.stop();
          try {
            simpleSession0.validate();
            fail("Expecting exception: StoppedSessionException");
          } catch(StoppedSessionException e) {
            /*
             * Session with id [null] has been explicitly stopped.  No further interaction under this session is allowed.
             */
          }
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test12()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.stop();
          boolean boolean0 = simpleSession0.isValid();
          assertEquals(true, simpleSession0.isStopped());
          assertEquals(false, boolean0);
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test13()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          boolean boolean0 = simpleSession0.isValid();
          assertEquals(1800000L, simpleSession0.getTimeout());
          assertEquals(true, boolean0);
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test14()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          assertEquals(false, simpleSession0.isTimedOut());
          
          simpleSession0.setExpired(true);
          boolean boolean0 = simpleSession0.isValid();
          assertEquals(true, simpleSession0.isExpired());
          assertEquals(false, boolean0);
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test15()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          boolean boolean0 = simpleSession0.isTimedOut();
          assertEquals(false, boolean0);
          assertEquals(1800000L, simpleSession0.getTimeout());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test16()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          HashMap<Object, Object> hashMap0 = new HashMap<Object, Object>();
          simpleSession0.setAttributes((Map<Object, Object>) hashMap0);
          simpleSession0.setAttribute((Object) "localhost", (Object) "localhost");
          assertEquals(1800000L, simpleSession0.getTimeout());
          assertEquals(false, simpleSession0.isExpired());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test17()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          SimpleSession simpleSession0 = new SimpleSession();
          HashMap<Object, Object> hashMap0 = new HashMap<Object, Object>();
          simpleSession0.setAttributes((Map<Object, Object>) hashMap0);
          Collection<Object> collection0 = simpleSession0.getAttributeKeys();
          assertNotNull(collection0);
          assertEquals(true, simpleSession0.isValid());
          assertEquals(1800000L, simpleSession0.getTimeout());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test18()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          SimpleSession simpleSession0 = new SimpleSession();
          Collection<Object> collection0 = simpleSession0.getAttributeKeys();
          assertEquals(1800000L, simpleSession0.getTimeout());
          assertEquals(true, simpleSession0.isValid());
          assertNotNull(collection0);
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test19()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          HashMap<Object, Object> hashMap0 = new HashMap<Object, Object>();
          simpleSession0.setAttributes((Map<Object, Object>) hashMap0);
          simpleSession0.getAttribute((Object) "localhost");
          assertEquals(true, simpleSession0.isValid());
          assertEquals(1800000L, simpleSession0.getTimeout());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test20()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.getAttribute((Object) "localhost");
          assertEquals(1800000L, simpleSession0.getTimeout());
          assertEquals(false, simpleSession0.isExpired());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test21()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
          SimpleSession simpleSession0 = new SimpleSession();
          simpleSession0.setAttribute((Object) null, (Object) null);
          assertEquals(1800000L, simpleSession0.getTimeout());
          assertEquals(true, simpleSession0.isValid());
          assertEquals(false, simpleSession0.isTimedOut());
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }
}