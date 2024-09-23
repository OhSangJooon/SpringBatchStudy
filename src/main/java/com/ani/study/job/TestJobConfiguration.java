package com.ani.study.job;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestJobConfiguration {

  private final JobRepository jobRepository;

  private final PlatformTransactionManager batchTransactionManager;

  private int itemReaderCnt = 1;

  @Bean
  public Job testJob() {
    return new JobBuilder("testJob", jobRepository)
        .start(testStep())
        .build();
  }

  @Bean
  public Step testStep() {
    return new StepBuilder("testStep", jobRepository)
        .<Map<String, String>, Map<String, String>>chunk(10, batchTransactionManager)
        .reader(testItemReader())
        .writer(testItemWriter())
        .build();
  }

  @Bean
  public ItemReader<Map<String, String>> testItemReader() {
    return () -> {
      if(itemReaderCnt == 5) return null;

      Map<String, String> testMap = new HashMap<>();
      testMap.put("test" + itemReaderCnt, "테스트" + itemReaderCnt);
      itemReaderCnt++;
      return testMap;
    };
  }

  @Bean
  public ItemWriter<Map<String, String>> testItemWriter() {
    return chunk -> {
      log.info("Chunk size: {}", chunk.size());
      chunk.forEach(item -> log.info("Processing item: {}", item));
    };
  }
}
