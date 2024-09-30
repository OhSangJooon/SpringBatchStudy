package com.ani.study.job;

import com.ani.study.common.BatchRetryableItemWriter;
import com.ani.study.common.RetryTemplateProvider;
import com.ani.study.exception.TestCusotmException;
import com.ani.study.exception.TestCusotmException2;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TestRetryJobConfiguration {

  private final JobRepository jobRepository;

  private final PlatformTransactionManager batchTransactionManager;
  private final RetryTemplateProvider retryTemplateProvider;

  private int itemReaderCnt = 0;

  @Bean
  public Job testRetryJob() {
    return new JobBuilder("testRetryJob", jobRepository)
        .start(testRetryStep())
        .build();
  }

  @Bean
  public Step testRetryStep() {
    // 재시도 대상 예외 목록 설정
    List<Class<? extends Throwable>> retryableExceptionList = List.of(TestCusotmException.class, TestCusotmException2.class);
    RetryTemplate retryTemplate = retryTemplateProvider.createRetryTemplate(2, 3000L, retryableExceptionList);

    return new StepBuilder("testRetryStep", jobRepository)
        .<Map<String, Integer>, Map<String, Integer>>chunk(5, batchTransactionManager)
        .reader(testRetryItemReader())
        .processor(testRetryItemProcessor())
        .writer(new BatchRetryableItemWriter<>(testRetryItemWriter(), retryTemplate, retryableExceptionList))
        .build();
  }

  @Bean
  public ItemReader<Map<String, Integer>> testRetryItemReader() {
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
  public ItemProcessor<Map<String, Integer>, Map<String, Integer>> testRetryItemProcessor() {
    return item -> {
      log.info("ItemProcessor item : {}", item);
      return item;
    };
  }

  @Bean
  public ItemWriter<Map<String, Integer>> testRetryItemWriter() {
    return chunk -> {
      log.info("ItemWriter items : {}", chunk);

      chunk.forEach(item -> {
        log.info("ItemWriter : item : {}", item.toString());
        item.forEach((key, value) -> {
          if(value == 3 ) {
            throw new TestCusotmException();
          }
        });
      });
    };
  }
}
