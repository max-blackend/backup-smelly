/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.cache.ehcache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import net.sf.ehcache.CacheManager;
import org.evosuite.Properties.SandboxMode;
import org.evosuite.sandbox.Sandbox;
import org.jsecurity.cache.CacheException;
import org.jsecurity.cache.ehcache.EhCache;
import org.jsecurity.cache.ehcache.EhCacheManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class EhCacheManagerEvoSuiteTest {

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
          EhCacheManager ehCacheManager0 = new EhCacheManager();
          ehCacheManager0.init();
          EhCache ehCache0 = (EhCache)ehCacheManager0.getCache("jsecurity-activeSessionCache");
          assertEquals("EhCache [jsecurity-activeSessionCache]", ehCache0.toString());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test1()  throws Throwable  {
      EhCacheManager ehCacheManager0 = new EhCacheManager();
      ehCacheManager0.setCacheManagerConfigFile("#Y;MTa+NpdL");
      assertEquals("#Y;MTa+NpdL", ehCacheManager0.getCacheManagerConfigFile());
  }

  @Test
  public void test2()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          EhCacheManager ehCacheManager0 = new EhCacheManager();
          ehCacheManager0.init();
          ehCacheManager0.getCache("#Y;MTa+NpdL");
          EhCache ehCache0 = (EhCache)ehCacheManager0.getCache("#Y;MTa+NpdL");
          assertEquals("EhCache [#Y;MTa+NpdL]", ehCache0.toString());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test3()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          EhCacheManager ehCacheManager0 = new EhCacheManager();
          ehCacheManager0.init();
          ehCacheManager0.init();
          assertEquals("classpath:org/jsecurity/cache/ehcache/ehcache.xml", ehCacheManager0.getCacheManagerConfigFile());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }

  @Test
  public void test4()  throws Throwable  {
      EhCacheManager ehCacheManager0 = new EhCacheManager();
      ehCacheManager0.destroy();
      assertEquals("classpath:org/jsecurity/cache/ehcache/ehcache.xml", ehCacheManager0.getCacheManagerConfigFile());
  }

  @Test
  public void test5()  throws Throwable  {
    Future<?> future = executor.submit(new Runnable(){ 
            public void run() { 
        try {
          EhCacheManager ehCacheManager0 = new EhCacheManager();
          ehCacheManager0.init();
          ehCacheManager0.setCacheManager((CacheManager) null);
          ehCacheManager0.destroy();
          assertEquals("classpath:org/jsecurity/cache/ehcache/ehcache.xml", ehCacheManager0.getCacheManagerConfigFile());
        } catch(Throwable t) {
            // Need to catch declared exceptions
        }
      } 
    }); 
    future.get(6000, TimeUnit.MILLISECONDS); 
  }
}