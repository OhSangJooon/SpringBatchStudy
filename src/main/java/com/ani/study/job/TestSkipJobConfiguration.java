package com.ani.study.job;

import com.ani.study.domain.service.TestService;
import com.ani.study.exception.TestCusotmException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestSkipJobConfiguration {

  private final JobRepository jobRepository;

  private final PlatformTransactionManager batchTransactionManager;

  private final TestService testService;

  private int itemReaderCnt = 0;

  @Bean
  public Job testJob() {
    return new JobBuilder("testJob", jobRepository)
        .start(testStep())
        .build();
  }

  @Bean
  public Step testStep() {
    return new StepBuilder("testStep", jobRepository)
        .<Map<String, Integer>, Map<String, Integer>>chunk(5, batchTransactionManager)
        .reader(testItemReader())
        .processor(testItemProcessor())
        .writer(testItemWriter())
        .faultTolerant()  // 내결함성 활성화
        .skip(TestCusotmException.class) // 커스텀 예외 발생시 Skip 발생
        .skipLimit(3) // 최대 3번까지 스킵 허용
        .build();
  }

  @Bean
  public ItemReader<Map<String, Integer>> testItemReader() {
    return () -> {
      itemReaderCnt++;
      if(itemReaderCnt == 11) return null;

      Map<String, Integer> testMap = new HashMap<>();
      testMap.put("test" + itemReaderCnt, itemReaderCnt);
      log.info("itemReaderMap : {}", testMap);
      return testMap;
    };
  }

  @Bean
  public ItemProcessor<Map<String, Integer>, Map<String, Integer>> testItemProcessor() {
    return item -> {
      log.info("ItemProcessor item : {}", item);
      return item;
    };
  }

  @Bean
  public ItemWriter<Map<String, Integer>> testItemWriter() {
    return chunk -> {
      log.info("ItemWriter items : {}", chunk);
      chunk.getItems().forEach(testService::memberUpdate);
    };
  }
}
