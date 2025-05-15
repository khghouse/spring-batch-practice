package com.practice.batch.config;

import com.practice.batch.dto.CustomerCsvDto;
import com.practice.batch.entity.Customer;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CustomerJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job customerJob() {
        return new JobBuilder("customerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customerStep())
                .build();
    }

    @Bean
    public Step customerStep() {
        return new StepBuilder("customerStep", jobRepository)
                .<CustomerCsvDto, Customer>chunk(10, transactionManager)
                .reader(csvReader()) // csv 파일에서 CustomerCsvDto를 읽는다.
                .processor(customerProcessor()) // dto를 entity로 변환
                .writer(jpaWriter()) // Customer 엔티티를 db에 저장
                .build();
    }

    @Bean
    public FlatFileItemReader<CustomerCsvDto> csvReader() {
        return new FlatFileItemReaderBuilder<CustomerCsvDto>()
                .name("customerReader")
                .resource(new ClassPathResource("customers.csv"))
                .delimited()
                .names("id", "name", "email")
                .targetType(CustomerCsvDto.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerCsvDto, Customer> customerProcessor() {
        return customer -> new Customer(
                customer.getId(),
                customer.getName().toUpperCase(),
                customer.getEmail()
        );
    }

    @Bean
    public JpaItemWriter<Customer> jpaWriter() {
        JpaItemWriter<Customer> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
