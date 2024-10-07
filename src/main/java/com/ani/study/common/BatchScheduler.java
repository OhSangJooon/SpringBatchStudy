package com.ani.study.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class BatchScheduler extends QuartzJobBean {
  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private JobRegistry jobRegistry;

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    String jobName = context.getJobDetail().getKey().getName(); // JobDetail에서 잡 이름을 가져옴
    try {
      Job job = jobRegistry.getJob(jobName);  // JobRegistry에서 Job을 동적으로 조회
      jobLauncher.run(job, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
    } catch (Exception e) {
      log.error("fail to execute job : {}, {}", jobName, this.getClass().getSimpleName(), e);
      throw new JobExecutionException(e);
    }
  }
}
