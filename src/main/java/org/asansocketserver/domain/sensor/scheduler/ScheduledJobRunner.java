package org.asansocketserver.domain.sensor.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScheduledJobRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job processSensorDataJob;

    @Scheduled(cron = "0 29 20 * * ?")
    public void runJob() throws Exception {
        jobLauncher.run(processSensorDataJob, new JobParameters());
    }
}
