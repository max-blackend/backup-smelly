package org.jsecurity.web.attr;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.jsecurity.web.WebUtils.toHttp;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditor;
import csbst.testing.fitness.*;
public class CookieAttribute<T> extends AbstractWebAttribute<T> {
  private static final Log log=LogFactory.getLog(CookieAttribute.class);
  public static final int ONE_YEAR=60 * 60 * 24* 365;
  public static final int INDEFINITE=Integer.MAX_VALUE;
  public static final String DEFAULT_PATH=null;
  public static final int DEFAULT_MAX_AGE=-1;
  public static final boolean DEFAULT_SECURE=false;
  private String path=DEFAULT_PATH;
  private int maxAge=DEFAULT_MAX_AGE;
  private boolean secure=DEFAULT_SECURE;
  public CookieAttribute(){
    NumberCoveredBranches.maintainPathTrace(1,"org.jsecurity.web.attr.CookieAttribute");
  }
  public CookieAttribute(  String name){
    super(name);
    NumberCoveredBranches.maintainPathTrace(2,"org.jsecurity.web.attr.CookieAttribute");
  }
  public CookieAttribute(  String name,  String path){
    super(name);
    NumberCoveredBranches.maintainPathTrace(3,"org.jsecurity.web.attr.CookieAttribute");
    setPath(path);
  }
  public CookieAttribute(  String name,  int maxAge){
    super(name);
    NumberCoveredBranches.maintainPathTrace(4,"org.jsecurity.web.attr.CookieAttribute");
    setMaxAge(maxAge);
  }
  public CookieAttribute(  String name,  String path,  int maxAge){
    this(name,path);
    NumberCoveredBranches.maintainPathTrace(5,"org.jsecurity.web.attr.CookieAttribute");
    setMaxAge(maxAge);
  }
  public CookieAttribute(  String name,  String path,  int maxAge,  Class<? extends PropertyEditor> editorClass){
    super(name,editorClass);
    NumberCoveredBranches.maintainPathTrace(6,"org.jsecurity.web.attr.CookieAttribute");
    setPath(path);
    setMaxAge(maxAge);
  }
  public String getPath(){
    NumberCoveredBranches.maintainPathTrace(7,"org.jsecurity.web.attr.CookieAttribute");
    return path;
  }
  public void setPath(  String path){
    NumberCoveredBranches.maintainPathTrace(8,"org.jsecurity.web.attr.CookieAttribute");
    this.path=path;
  }
  public int getMaxAge(){
    NumberCoveredBranches.maintainPathTrace(9,"org.jsecurity.web.attr.CookieAttribute");
    return maxAge;
  }
  public void setMaxAge(  int maxAge){
    NumberCoveredBranches.maintainPathTrace(10,"org.jsecurity.web.attr.CookieAttribute");
    this.maxAge=maxAge;
  }
  public boolean isSecure(){
    NumberCoveredBranches.maintainPathTrace(11,"org.jsecurity.web.attr.CookieAttribute");
    return secure;
  }
  public void setSecure(  boolean secure){
    NumberCoveredBranches.maintainPathTrace(12,"org.jsecurity.web.attr.CookieAttribute");
    this.secure=secure;
  }
  private static Cookie getCookie(  HttpServletRequest request,  String cookieName){
    NumberCoveredBranches.maintainPathTrace(13,"org.jsecurity.web.attr.CookieAttribute");
    Cookie cookies[]=request.getCookies();
    if (cookies != null) {
      NumberCoveredBranches.maintainPathTrace(14,"org.jsecurity.web.attr.CookieAttribute");
      for (      Cookie cookie : cookies) {
        if (cookie.getName().equals(cookieName)) {
          NumberCoveredBranches.maintainPathTrace(16,"org.jsecurity.web.attr.CookieAttribute");
          return cookie;
        }
 else {
          NumberCoveredBranches.maintainPathTrace(17,"org.jsecurity.web.attr.CookieAttribute");
        }
      }
    }
 else {
      NumberCoveredBranches.maintainPathTrace(15,"org.jsecurity.web.attr.CookieAttribute");
    }
    return null;
  }
  public T onRetrieveValue(  ServletRequest request,  ServletResponse response){
    NumberCoveredBranches.maintainPathTrace(18,"org.jsecurity.web.attr.CookieAttribute");
    T value=null;
    String stringValue;
    Cookie cookie=getCookie(toHttp(request),getName());
    if (cookie != null && cookie.getMaxAge() != 0) {
      NumberCoveredBranches.maintainPathTrace(19,"org.jsecurity.web.attr.CookieAttribute");
      stringValue=cookie.getValue();
      if (log.isInfoEnabled()) {
        NumberCoveredBranches.maintainPathTrace(21,"org.jsecurity.web.attr.CookieAttribute");
        log.info("Found string value [" + stringValue + "] from HttpServletRequest Cookie ["+ getName()+ "]");
      }
 else {
        NumberCoveredBranches.maintainPathTrace(22,"org.jsecurity.web.attr.CookieAttribute");
      }
      value=fromStringValue(stringValue);
    }
 else {
      NumberCoveredBranches.maintainPathTrace(20,"org.jsecurity.web.attr.CookieAttribute");
      if (log.isDebugEnabled()) {
        NumberCoveredBranches.maintainPathTrace(23,"org.jsecurity.web.attr.CookieAttribute");
        log.debug("No value found in request Cookies under cookie name [" + getName() + "]");
      }
 else {
        NumberCoveredBranches.maintainPathTrace(24,"org.jsecurity.web.attr.CookieAttribute");
      }
    }
    return value;
  }
  public void onStoreValue(  T value,  ServletRequest servletRequest,  ServletResponse servletResponse){
    NumberCoveredBranches.maintainPathTrace(25,"org.jsecurity.web.attr.CookieAttribute");
    HttpServletRequest request=toHttp(servletRequest);
    HttpServletResponse response=toHttp(servletResponse);
    String name=getName();
    int maxAge=getMaxAge();
    if (getPath() != null) {
      NumberCoveredBranches.maintainPathTrace(26,"org.jsecurity.web.attr.CookieAttribute");
    }
 else {
      NumberCoveredBranches.maintainPathTrace(27,"org.jsecurity.web.attr.CookieAttribute");
    }
    String path=getPath() != null ? getPath() : request.getContextPath();
    String stringValue=toStringValue(value);
    Cookie cookie=new Cookie(name,stringValue);
    cookie.setMaxAge(maxAge);
    cookie.setPath(path);
    if (isSecure()) {
      NumberCoveredBranches.maintainPathTrace(28,"org.jsecurity.web.attr.CookieAttribute");
      cookie.setSecure(true);
    }
 else {
      NumberCoveredBranches.maintainPathTrace(29,"org.jsecurity.web.attr.CookieAttribute");
    }
    response.addCookie(cookie);
    if (log.isTraceEnabled()) {
      NumberCoveredBranches.maintainPathTrace(30,"org.jsecurity.web.attr.CookieAttribute");
      log.trace("Added Cookie [" + name + "] to path ["+ path+ "] with value ["+ stringValue+ "] to the HttpServletResponse.");
    }
 else {
      NumberCoveredBranches.maintainPathTrace(31,"org.jsecurity.web.attr.CookieAttribute");
    }
  }
  public void removeValue(  ServletRequest servletRequest,  ServletResponse response){
    NumberCoveredBranches.maintainPathTrace(32,"org.jsecurity.web.attr.CookieAttribute");
    HttpServletRequest request=toHttp(servletRequest);
    Cookie cookie=getCookie(request,getName());
    if (cookie != null) {
      NumberCoveredBranches.maintainPathTrace(33,"org.jsecurity.web.attr.CookieAttribute");
      cookie.setMaxAge(0);
      if (getPath() == null) {
        NumberCoveredBranches.maintainPathTrace(35,"org.jsecurity.web.attr.CookieAttribute");
      }
 else {
        NumberCoveredBranches.maintainPathTrace(36,"org.jsecurity.web.attr.CookieAttribute");
      }
      cookie.setPath(getPath() == null ? request.getContextPath() : getPath());
      cookie.setSecure(isSecure());
      toHttp(response).addCookie(cookie);
    }
 else {
      NumberCoveredBranches.maintainPathTrace(34,"org.jsecurity.web.attr.CookieAttribute");
    }
  }
}
