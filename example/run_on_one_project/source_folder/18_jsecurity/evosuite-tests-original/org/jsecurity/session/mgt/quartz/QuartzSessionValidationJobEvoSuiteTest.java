/*
 * This file was automatically generated by EvoSuite
 */

package org.jsecurity.session.mgt.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.junit.EvoSuiteRunner;
import static org.junit.Assert.*;
import java.util.Date;
import org.jsecurity.session.mgt.quartz.QuartzSessionValidationJob;
import org.junit.BeforeClass;
import org.quartz.Calendar;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.NthIncludedDayTrigger;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.TriggerFiredBundle;

@RunWith(EvoSuiteRunner.class)
public class QuartzSessionValidationJobEvoSuiteTest {

  @BeforeClass 
  public static void initEvoSuiteFramework(){ 
    org.evosuite.Properties.REPLACE_CALLS = true; 
  } 


  @Test
  public void test0()  throws Throwable  {
      QuartzSessionValidationJob quartzSessionValidationJob0 = new QuartzSessionValidationJob();
      Class<?> class0 = QuartzSessionValidationJob.class;
      JobDetail jobDetail0 = new JobDetail("3:KX{&-yP^.", "3:KX{&-yP^.", (Class) class0, true, true, true);
      SimpleTrigger simpleTrigger0 = new SimpleTrigger();
      NthIncludedDayTrigger nthIncludedDayTrigger0 = new NthIncludedDayTrigger();
      Date date0 = nthIncludedDayTrigger0.computeFirstFireTime((Calendar) null);
      TriggerFiredBundle triggerFiredBundle0 = new TriggerFiredBundle(jobDetail0, (Trigger) simpleTrigger0, (Calendar) null, true, date0, date0, date0, date0);
      JobExecutionContext jobExecutionContext0 = new JobExecutionContext((Scheduler) null, triggerFiredBundle0, (Job) quartzSessionValidationJob0);
      // Undeclared exception!
      try {
        quartzSessionValidationJob0.execute(jobExecutionContext0);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }
}