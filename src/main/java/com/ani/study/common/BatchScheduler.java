package com.ani.study.common;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@RequiredArgsConstructor
//@Component
public class BatchScheduler {
//  @Autowired
//  private JobExplorer jobExplorer;
//
//  @Autowired
//  private JobLauncher jobLauncher;
//
//  @Autowired
//  private JobRegistry jobRegistry;
//
//  @Autowired
//  private Scheduler scheduler;
//
//  @Bean(name = "testJobRegistryBeanPostProcessor")
//  public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
//    JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
//    jobProcessor.setJobRegistry(jobRegistry);
//    return jobProcessor;
//  }
//
//  @Scheduled(cron = "0/10 * * * * *") // 10초마다 실행
//  public void runJob() {
//    String time = LocalDateTime.now().toString();
//    try {
//      Job job = jobRegistry.getJob("testJob"); // job 이름
//      JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
//      jobLauncher.run(job, jobParam.toJobParameters());
//    } catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
//             JobParametersInvalidException | JobRestartException e) {
//      throw new RuntimeException(e);
//    }
//  }
}
