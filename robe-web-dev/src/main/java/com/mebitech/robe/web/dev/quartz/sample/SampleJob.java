package com.mebitech.robe.web.dev.quartz.sample;

import com.mebitech.robe.quartz.RobeJob;
import com.mebitech.robe.quartz.RobeTrigger;
import com.mebitech.robe.quartz.info.TriggerInfo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@RobeJob(name = "SampleJob", description = "Sample Quartz Job for a demonstration.", triggers = {
        @RobeTrigger(cron = "0/4 * * * * ?", name = "Every 4 seconds", group = "Sample", type = TriggerInfo.Type.CRON),
        @RobeTrigger(cron = "0/6 * * * * ?", name = "Every 6 seconds", group = "Sample", type = TriggerInfo.Type.CRON),
        @RobeTrigger(name = "On App Start", group = "Sample", type = TriggerInfo.Type.ON_APP_START),
        @RobeTrigger(name = "On App Stop", group = "Sample", type = TriggerInfo.Type.ON_APP_STOP)
})
public class SampleJob implements Job {
    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("JOb running NextFireTime: {},ApplicationContext: {}", jobExecutionContext.getNextFireTime(), applicationContext);

    }
}
