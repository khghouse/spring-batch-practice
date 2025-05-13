package com.practice.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job helloJob;
    private final Job customerJob;

    @Override
    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestTime", LocalDateTime.now().toString())
                .toJobParameters();

        jobLauncher.run(helloJob, jobParameters);
        jobLauncher.run(customerJob, jobParameters);
    }

}
