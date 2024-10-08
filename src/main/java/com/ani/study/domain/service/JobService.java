package com.ani.study.domain.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
  private final JobLauncher jobLauncher;
  private final ApplicationContext context;

  /**
   * Spring Batch Job 을 실행한다.
   * @param jobName batch job name
   *
   * */
  @Async
  public void invokeJob(String jobName) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    jobParametersBuilder.addString("Sync date", date);

    log.info("job parameters info > : {}", jobParametersBuilder.toJobParameters().getParameters());
    var jobToStart = context.getBean(jobName, Job.class); // Job 빈을 스프링 컨텍스트에서 가져옴
    jobLauncher.run(jobToStart, jobParametersBuilder.toJobParameters()); // Job을 실행하고, 해당 작업에 JobParameters를 전달
  }
}
