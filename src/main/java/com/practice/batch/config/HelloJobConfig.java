package com.practice.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfig {

    private final JobRepository jobRepository; // Job 실행 및 상태를 저장, 관리하는 핵심 컴포넌트
    private final PlatformTransactionManager transactionManager; // 트랜잭션 관리, tasklet() 또는 chunk 기반 작업 실행 시 필요

    /**
     * Job 인스턴스 생성
     */
    @Bean
    public Job helloJob() {
        return new JobBuilder("helloJob", jobRepository) // jobRepository를 넘겨 Job 상태 관리
                .incrementer(new RunIdIncrementer())
                .start(helloStep()) // helloStep()으로 정의된 Step 하나로 시작
                .build();
    }

    /**
     * Step 정의
     */
    @Bean
    public Step helloStep() {
        return new StepBuilder("helloStep", jobRepository) // jobRepository를 넘겨 Step 상태 관리
                .tasklet(tasklet(), transactionManager) // Step은 tasklet()이라는 작업 단위로 실행
                .build();
    }

    /**
     * Tasklet : 단일 작업 처리 단위를 정의
     */
    @Bean
    public Tasklet tasklet() {
        // contribution : Step의 실행 상태를 담고 있음
        // chunkContext : Step 내의 실행 컨텍스트 (JobParameter 등 접근 가능)
        return ((contribution, chunkContext) -> {
            // 작업 내용
            System.out.println(">>> Hello Spring Batch 5!");
            return RepeatStatus.FINISHED; // 한번 실행 후 종료
        });
    }

}
