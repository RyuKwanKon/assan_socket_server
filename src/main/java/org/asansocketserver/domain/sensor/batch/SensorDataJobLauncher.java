package org.asansocketserver.domain.sensor.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SensorDataJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job sensorDataJob;

    @Autowired
    public SensorDataJobLauncher(JobLauncher jobLauncher, Job sensorDataJob) {
        this.jobLauncher = jobLauncher;
        this.sensorDataJob = sensorDataJob;
    }

    @Scheduled(cron = "0 23 19 * * *")// 예: 매 정시마다 실행
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(sensorDataJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
