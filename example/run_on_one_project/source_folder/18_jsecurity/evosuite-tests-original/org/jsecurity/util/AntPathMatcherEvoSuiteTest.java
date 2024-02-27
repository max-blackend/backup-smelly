/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import org.jsecurity.util.AntPathMatcher;
import org.junit.BeforeClass;

@RunWith(EvoSuiteRunner.class)
public class AntPathMatcherEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matches("/'~p21^>s*zqriyr o^", "");
      assertEquals(false, boolean0);
  }

  @Test
  public void test1()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matchStart("", "*");
      assertEquals(false, boolean0);
  }

  @Test
  public void test2()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      antPathMatcher0.setPathSeparator((String) null);
  }

  @Test
  public void test3()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      antPathMatcher0.setPathSeparator("/**");
      boolean boolean0 = antPathMatcher0.doMatch("**", "**/**", true);
      assertEquals(false, boolean0);
  }

  @Test
  public void test4()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.isPattern("*");
      assertEquals(true, boolean0);
  }

  @Test
  public void test5()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.isPattern("!STq?pMwJ:wgeJg");
      assertEquals(true, boolean0);
  }

  @Test
  public void test6()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.isPattern("$k%");
      assertEquals(false, boolean0);
  }

  @Test
  public void test7()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.doMatch("**", "", true);
      assertEquals(true, boolean0);
  }

  @Test
  public void test8()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.match("**/**", "**/**");
      assertEquals(true, boolean0);
  }

  @Test
  public void test9()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.match("*:>Y*<ot:F(T$K", "RK#G!HKI}:Oj");
      assertEquals(false, boolean0);
  }

  @Test
  public void test10()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matches("/'~p21^>s*zqriyr o^/", "/'~p21^>s*zqriyr o^/");
      assertEquals(true, boolean0);
  }

  @Test
  public void test11()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matches("/**?eVbTw(he\"", "/**?eVbTw(he\"");
      assertEquals(true, boolean0);
  }

  @Test
  public void test12()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.doMatch("'~p21^>s*zqriyr o^", "", false);
      assertEquals(true, boolean0);
  }

  @Test
  public void test13()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matches("@ t]W.*/jI(.kA<", "");
      assertEquals(false, boolean0);
  }

  @Test
  public void test14()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.match("*", "");
      assertEquals(false, boolean0);
  }

  @Test
  public void test15()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      antPathMatcher0.setPathSeparator("");
      boolean boolean0 = antPathMatcher0.match("*", "");
      assertEquals(true, boolean0);
  }

  @Test
  public void test16()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matchStart("**/*", "**/*");
      assertEquals(true, boolean0);
  }

  @Test
  public void test17()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.match("**/*", "$k%");
      assertEquals(true, boolean0);
  }

  @Test
  public void test18()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.doMatch("//**/;**", "/R=u3v&'hhdC@", true);
      assertEquals(false, boolean0);
  }

  @Test
  public void test19()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matches("/**/*f*/**", "/**/*f*/**");
      assertEquals(true, boolean0);
  }

  @Test
  public void test20()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matches("/**/*f*/**", "/'~p21^>s*zqriyr o^/");
      assertEquals(false, boolean0);
  }

  @Test
  public void test21()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matchStart("h", "=");
      assertEquals(false, boolean0);
  }

  @Test
  public void test22()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matchStart("F", "|*AF");
      assertEquals(false, boolean0);
  }

  @Test
  public void test23()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matches("|$d>HqMIb2/<a/J?I", "|$d>HqMIb2/<a/J?I");
      assertEquals(true, boolean0);
  }

  @Test
  public void test24()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matchStart("8[fOy*ue(0Np^d!r", "8");
      assertEquals(false, boolean0);
  }

  @Test
  public void test25()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.match("Z]^d.?*}9tS@T{Z]^d.?*9tS@T{", "Z]^d.?*}9tS@T{Z]^d.?*9tS@T{");
      assertEquals(true, boolean0);
  }

  @Test
  public void test26()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.match("|*AF", "|");
      assertEquals(false, boolean0);
  }

  @Test
  public void test27()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      boolean boolean0 = antPathMatcher0.matchStart("*9<", "<");
      assertEquals(false, boolean0);
  }

  @Test
  public void test28()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      String string0 = antPathMatcher0.extractPathWithinPattern("'~p21^>s*zqriyr o^", "'~p21^>s*zqriyr o^");
      assertEquals("/'~p21^>s*zqriyr o^", string0);
      assertNotNull(string0);
  }

  @Test
  public void test29()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      String string0 = antPathMatcher0.extractPathWithinPattern("@ t]W.*/jI(.kA<", "");
      assertEquals("", string0);
      assertNotNull(string0);
  }

  @Test
  public void test30()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      String string0 = antPathMatcher0.extractPathWithinPattern("|$d>HqMIb2/<a/J?I", "|$d>HqMIb2/<a/J?I");
      assertEquals("J?I", string0);
      assertNotNull(string0);
  }

  @Test
  public void test31()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      String string0 = antPathMatcher0.extractPathWithinPattern("/**/**", "/**/**");
      assertEquals("**/**", string0);
      assertNotNull(string0);
  }

  @Test
  public void test32()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      String string0 = antPathMatcher0.extractPathWithinPattern("/**?eVbTw(he\"", "vGrO4?/hp@RHB");
      assertNotNull(string0);
      assertEquals("vGrO4?/hp@RHB", string0);
  }

  @Test
  public void test33()  throws Throwable  {
      AntPathMatcher antPathMatcher0 = new AntPathMatcher();
      antPathMatcher0.setPathSeparator("U}<I-13OhGL+IG>+");
      String string0 = antPathMatcher0.extractPathWithinPattern("", "r5^>vN=m3-n");
      assertEquals("r5^U}<I-13OhGL+IG>+vN=mU}<I-13OhGL+IG>+n", string0);
      assertNotNull(string0);
  }
}