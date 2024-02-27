/**
 * Scaffolding file used to store all the setups needed to run 
 * tests automatically generated by EvoSuite
 * Thu Sep 29 18:41:30 GMT 2022
 */

package org.jsecurity;

import org.evosuite.runtime.annotation.EvoSuiteClassExclude;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.evosuite.runtime.sandbox.Sandbox;
import org.evosuite.runtime.sandbox.Sandbox.SandboxMode;

@EvoSuiteClassExclude
public class SecurityUtils_ESTest_scaffolding {

  @org.junit.Rule
  public org.evosuite.runtime.vnet.NonFunctionalRequirementRule nfr = new org.evosuite.runtime.vnet.NonFunctionalRequirementRule();

  private org.evosuite.runtime.thread.ThreadStopper threadStopper =  new org.evosuite.runtime.thread.ThreadStopper (org.evosuite.runtime.thread.KillSwitchHandler.getInstance(), 3000);


  @BeforeClass
  public static void initEvoSuiteFramework() { 
    org.evosuite.runtime.RuntimeSettings.className = "org.jsecurity.SecurityUtils"; 
    org.evosuite.runtime.GuiSupport.initialize(); 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfThreads = 100; 
    org.evosuite.runtime.RuntimeSettings.maxNumberOfIterationsPerLoop = 10000; 
    org.evosuite.runtime.RuntimeSettings.mockSystemIn = true; 
    org.evosuite.runtime.RuntimeSettings.sandboxMode = org.evosuite.runtime.sandbox.Sandbox.SandboxMode.RECOMMENDED; 
    org.evosuite.runtime.sandbox.Sandbox.initializeSecurityManagerForSUT(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.init();
    setSystemProperties();
    initializeClasses();
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
  } 

  @Before
  public void initTestCase(){ 
    threadStopper.storeCurrentThreads();
    threadStopper.startRecordingTime();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().initHandler(); 
    org.evosuite.runtime.sandbox.Sandbox.goingToExecuteSUTCode(); 
    org.evosuite.runtime.GuiSupport.setHeadless(); 
    org.evosuite.runtime.Runtime.getInstance().resetRuntime(); 
    org.evosuite.runtime.agent.InstrumentingAgent.activate(); 
  } 

  @After
  public void doneWithTestCase(){ 
    threadStopper.killAndJoinClientThreads();
    org.evosuite.runtime.jvm.ShutdownHookHandler.getInstance().safeExecuteAddedHooks(); 
    org.evosuite.runtime.classhandling.JDKClassResetter.reset(); 
    resetClasses(); 
    org.evosuite.runtime.sandbox.Sandbox.doneWithExecutingSUTCode(); 
    org.evosuite.runtime.agent.InstrumentingAgent.deactivate(); 
    org.evosuite.runtime.GuiSupport.restoreHeadlessMode(); 
  } 

  public static void setSystemProperties() {
 
    /*No java.lang.System property to set*/
  }

  private static void initializeClasses() {
    org.evosuite.runtime.classhandling.ClassStateSupport.initializeClasses(SecurityUtils_ESTest_scaffolding.class.getClassLoader() ,
      "org.jsecurity.session.SessionException",
      "org.jsecurity.authz.UnauthorizedException",
      "org.jsecurity.authc.AuthenticationInfo",
      "org.jsecurity.mgt.SecurityManager",
      "org.jsecurity.subject.Subject",
      "org.jsecurity.session.Session",
      "org.jsecurity.authz.AuthorizationException",
      "org.jsecurity.SecurityUtils",
      "org.jsecurity.authc.AuthenticationToken",
      "org.jsecurity.authc.AuthenticationException",
      "org.jsecurity.authc.Authenticator",
      "org.jsecurity.session.SessionFactory",
      "org.jsecurity.authz.Permission",
      "org.jsecurity.subject.PrincipalCollection",
      "org.jsecurity.session.InvalidSessionException",
      "org.jsecurity.authz.HostUnauthorizedException",
      "org.jsecurity.authz.Authorizer",
      "org.jsecurity.JSecurityException"
    );
  } 

  private static void resetClasses() {
    org.evosuite.runtime.classhandling.ClassResetter.getInstance().setClassLoader(SecurityUtils_ESTest_scaffolding.class.getClassLoader()); 

    org.evosuite.runtime.classhandling.ClassStateSupport.resetClasses(
      "org.jsecurity.SecurityUtils",
      "org.jsecurity.util.ThreadContext",
      "org.jsecurity.authz.ModularRealmAuthorizer",
      "org.jsecurity.subject.AbstractRememberMeManager",
      "org.jsecurity.realm.CachingRealm",
      "org.jsecurity.realm.AuthenticatingRealm",
      "org.jsecurity.mgt.CachingSecurityManager",
      "org.jsecurity.subject.SimplePrincipalCollection",
      "org.jsecurity.authc.SimpleAccount"
    );
  }
}
