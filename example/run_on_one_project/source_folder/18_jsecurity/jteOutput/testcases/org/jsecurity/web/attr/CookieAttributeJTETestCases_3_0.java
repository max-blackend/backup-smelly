package org.jsecurity.web.attr;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Rule;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.jsecurity.web.attr.CookieAttribute;
/** 
 * This class was automatically generated to test the org.jsecurity.web.attr.CookieAttribute class according to all branches coverage criterion
ExceptionsOriented: false 
projectPackagesPrefix:org.jsecurity.web.attr 
Covered branches: [32, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 18, 25, 26, 27, 29]
Uncovered branches: [33, 34, 35, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 28, 30, 31]
Total number of branches: 36
Total number of covered branches: 19
Coverage : 52.78%
Evaluations : 5282
search time (ms): 42521
total runtime (ms): 44750
 */
public class CookieAttributeJTETestCases_3_0 {
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, String, -711, class java.lang.String]
2)----->getPath[], 
3)----->getPath[]
 Covered Branches:[6, 7, 8, 10]
 */
  @Test public void TestCase0() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("zGwmCy3dI5pGclbiTlp19d");
    int clsUTCookieAttributeP1P3=-711;
    Class clsUTCookieAttributeP1P4=String.class;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,(String)null,clsUTCookieAttributeP1P3,clsUTCookieAttributeP1P4);
    String clsUTCookieAttributeP2R=null;
      clsUTCookieAttributeP2R=clsUTCookieAttribute.getPath();
    assertNull(clsUTCookieAttributeP2R);
    String clsUTCookieAttributeP3R=null;
      clsUTCookieAttributeP3R=clsUTCookieAttribute.getPath();
    assertNull(clsUTCookieAttributeP3R);
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, String, 516]
2)----->removeValue[null, null]
 Covered Branches:[32, 3, 5, 8, 10, 13]
 */
  @Test public void TestCase1() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("=^e]");
    String clsUTCookieAttributeP1P2=new String("43s^iYYTEleb@mQ;cSsRuuNzS/?>_gt>1d ds<}x;eWy0pwvED9{hH_eDEd?IC>uq}OCLyDEdEi7iTz.G.TMW.}k_QP#zaj[V2Ar*hSno-|1V!Cw/s+[)V{?evym!AW(yEE1_rv");
    int clsUTCookieAttributeP1P3=516;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,clsUTCookieAttributeP1P2,clsUTCookieAttributeP1P3);
//Exception
    try {
      clsUTCookieAttribute.removeValue((ServletRequest)null,(ServletResponse)null);
		fail("Expected Exception");
    }
 catch (    Throwable exce) {
    }
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, String, 0]
2)----->onStoreValue[false, null, null]
 Covered Branches:[3, 5, 7, 8, 25, 9, 10, 26]
 */
  @Test public void TestCase2() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("bg53~}]v8h88vn<h%=0P*;kNiQ(ZSP}c");
    String clsUTCookieAttributeP1P2=new String("D&MN");
    int clsUTCookieAttributeP1P3=0;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,clsUTCookieAttributeP1P2,clsUTCookieAttributeP1P3);
    Boolean clsUTCookieAttributeP2P1O0=false;
    Object clsUTCookieAttributeP2P1=clsUTCookieAttributeP2P1O0;
//Exception
    try {
      clsUTCookieAttribute.onStoreValue((Object)clsUTCookieAttributeP2P1,(ServletRequest)null,(ServletResponse)null);
		fail("Expected Exception");
    }
 catch (    Throwable exce) {
    }
    assertEquals("false",clsUTCookieAttributeP2P1.toString());
    assertEquals(1237,clsUTCookieAttributeP2P1.hashCode());
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, String, 920]
2)----->onStoreValue[String, null, null]
 Covered Branches:[3, 5, 7, 8, 25, 9, 10, 26, 11, 29]
 */
  @Test public void TestCase3() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("583msdl0kg6tjo4ucfqpaenwry921hvix7bz");
    String clsUTCookieAttributeP1P2=new String("*hx5P}}uTXt");
    int clsUTCookieAttributeP1P3=920;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,clsUTCookieAttributeP1P2,clsUTCookieAttributeP1P3);
    String clsUTCookieAttributeP2P1O0=new String("2p62lwxjg88t9sr5al0qclmg3ax6hx3gsytvu12wdkl35wwlu5lgyedfie0dad4o216rpk78o5c7yrtavof4hxd6bntahtxoinax92y2d20nqlrm7s6llc5j2pkf6vebn02s35x3dqj3q1gif5o9v42r0k1mdh2r2401vt1y77agdieaclatkhdii9bpngrstdix");
    Object clsUTCookieAttributeP2P1=clsUTCookieAttributeP2P1O0;
//Exception
    try {
      clsUTCookieAttribute.onStoreValue((Object)clsUTCookieAttributeP2P1,(ServletRequest)null,(ServletResponse)null);
		fail("Expected Exception");
    }
 catch (    Throwable exce) {
    }
    assertEquals("2p62lwxjg88t9sr5al0qclmg3ax6hx3gsytvu12wdkl35wwlu5lgyedfie0dad4o216rpk78o5c7yrtavof4hxd6bntahtxoinax92y2d20nqlrm7s6llc5j2pkf6vebn02s35x3dqj3q1gif5o9v42r0k1mdh2r2401vt1y77agdieaclatkhdii9bpngrstdix",clsUTCookieAttributeP2P1.toString());
    assertEquals(-895994092,clsUTCookieAttributeP2P1.hashCode());
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String]
2)----->isSecure[]
 Covered Branches:[2, 11]
 */
  @Test public void TestCase4() throws Throwable {
    String clsUTCookieAttributeP1P1=new String(":[=1>?<4$5#-9} |_6/`](+;&~_%{");
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1);
    boolean clsUTCookieAttributeP2R=false;
      clsUTCookieAttributeP2R=clsUTCookieAttribute.isSecure();
    assertEquals(false,clsUTCookieAttributeP2R);
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, String, 835]
2)----->isSecure[], 
3)----->setMaxAge[-685], 
4)----->setSecure[true]
 Covered Branches:[3, 5, 8, 10, 11, 12]
 */
  @Test public void TestCase5() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("pu%fb/b#j_$^bdla`=q@?^?.`i ~l]|-sh&*r$))p`m)}");
    int clsUTCookieAttributeP1P3=835;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,(String)null,clsUTCookieAttributeP1P3);
    boolean clsUTCookieAttributeP2R=false;
      clsUTCookieAttributeP2R=clsUTCookieAttribute.isSecure();
    assertEquals(false,clsUTCookieAttributeP2R);
    int clsUTCookieAttributeP3P1=-685;
      clsUTCookieAttribute.setMaxAge(clsUTCookieAttributeP3P1);
    boolean clsUTCookieAttributeP4P1=true;
      clsUTCookieAttribute.setSecure(clsUTCookieAttributeP4P1);
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, 786]
2)----->getPath[]
 Covered Branches:[4, 7, 10]
 */
  @Test public void TestCase6() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("");
    int clsUTCookieAttributeP1P2=786;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,clsUTCookieAttributeP1P2);
    String clsUTCookieAttributeP2R=null;
      clsUTCookieAttributeP2R=clsUTCookieAttribute.getPath();
    assertNull(clsUTCookieAttributeP2R);
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, -643]
2)----->getMaxAge[]
 Covered Branches:[4, 9, 10]
 */
  @Test public void TestCase7() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("TQzd|w`O7BrXxo;Z)ecVJK_4Am<G5@ua(W[}?_p]");
    int clsUTCookieAttributeP1P2=-643;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,clsUTCookieAttributeP1P2);
    int clsUTCookieAttributeP2R=0;
      clsUTCookieAttributeP2R=clsUTCookieAttribute.getMaxAge();
    assertEquals(-643,clsUTCookieAttributeP2R);
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, String, 620, class java.lang.Integer]
2)----->onRetrieveValue[null, null]
 Covered Branches:[18, 6, 8, 10, 13]
 */
  @Test public void TestCase8() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("bQcav*iMtfP5OY7@S)g<w_F0eBy^Dl1$4");
    String clsUTCookieAttributeP1P2=new String("75v<*e");
    int clsUTCookieAttributeP1P3=620;
    Class clsUTCookieAttributeP1P4=Integer.class;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,clsUTCookieAttributeP1P2,clsUTCookieAttributeP1P3,clsUTCookieAttributeP1P4);
    Object clsUTCookieAttributeP2R=null;
//Exception
    try {
      clsUTCookieAttributeP2R=clsUTCookieAttribute.onRetrieveValue((ServletRequest)null,(ServletResponse)null);
		fail("Expected Exception");
    }
 catch (    Throwable exce) {
    }
    assertNull(clsUTCookieAttributeP2R);
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[]
2)----->setPath[String], 
3)----->isMutable[]
 Covered Branches:[1, 8]
 */
  @Test public void TestCase9() throws Throwable {
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute();
      clsUTCookieAttribute.setPath((String)null);
    boolean clsUTCookieAttributeP3R=false;
      clsUTCookieAttributeP3R=clsUTCookieAttribute.isMutable();
    assertEquals(true,clsUTCookieAttributeP3R);
  }
  /** 
 * Chromosome :
1)----->org.jsecurity.web.attr.CookieAttribute[String, -392]
2)----->onStoreValue[true, null, null]
 Covered Branches:[4, 7, 25, 9, 10, 27]
 */
  @Test public void TestCase10() throws Throwable {
    String clsUTCookieAttributeP1P1=new String("a5y]sc:q]:.fux&?fk0e/$d $b^|fyqv`;jsrbaji)sm>2jc");
    int clsUTCookieAttributeP1P2=-392;
    CookieAttribute clsUTCookieAttribute=null;
      clsUTCookieAttribute=new CookieAttribute(clsUTCookieAttributeP1P1,clsUTCookieAttributeP1P2);
    boolean clsUTCookieAttributeP2P1O0=true;
    Object clsUTCookieAttributeP2P1=clsUTCookieAttributeP2P1O0;
//Exception
    try {
      clsUTCookieAttribute.onStoreValue((Object)clsUTCookieAttributeP2P1,(ServletRequest)null,(ServletResponse)null);
		fail("Expected Exception");
    }
 catch (    Throwable exce) {
    }
    assertEquals("true",clsUTCookieAttributeP2P1.toString());
    assertEquals(1231,clsUTCookieAttributeP2P1.hashCode());
  }
}
