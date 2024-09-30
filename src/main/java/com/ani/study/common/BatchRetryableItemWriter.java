package com.ani.study.common;

import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
public class BatchRetryableItemWriter<T> implements ItemWriter<T> {

  private final ItemWriter<T> itemWriter;
  private final RetryTemplate retryTemplate;
  private final List<Class<? extends Throwable>> retryExceptionList;

  public BatchRetryableItemWriter(ItemWriter<T> itemWriter, RetryTemplate retryTemplate, List<Class<? extends Throwable>> retryExceptionList) {
    this.itemWriter = itemWriter;
    this.retryTemplate = retryTemplate;
    this.retryExceptionList = retryExceptionList;
    // 현재 Chunk 단위로 Retry를 적용하고 있다. 청크를 무시하고 총 Retry 횟수를 지정하기 위해서는 필드에 maxRetry 를 선언 후
    // retry시 +1를 해준 뒤 failContext에 해당 retry 횟수가 되면 배치를 종료하면 된다.
  }

  @Override
  public void write(@NonNull Chunk<? extends T> chunk) throws Exception {
    retryTemplate.execute(
        context -> {
          try {
            itemWriter.write(chunk);
          } catch (Exception e) {
            if(retryExceptionList.contains(e.getClass())) {
              log.error("재시도 대상 예외 발생! 재시도 횟수 : {}", context.getRetryCount() + 1);
            }
            throw e;
          }

          return null; /* 성공한 경우 Null Retry 없음 */
        },
        failContext -> { // Recover 영역
          // 최대 재시도인 경우 로깅
          if(isMaxRetry(failContext)) {
            log.error("최대 재시도 횟수 초과. 재시도 횟수: {}", failContext.getRetryCount());
          }

          return null;
        });
  }

  /**
   * 현재 예외가 최대 재시도 횟수를 초과 했는지 확인한다.
   * */
  private boolean isMaxRetry(RetryContext context) {
    Object isExhausted = context.getAttribute(RetryContext.EXHAUSTED);

    return isRetryThrowable(context) && (isExhausted != null && (boolean) isExhausted);
  }

  /**
   * 마지막으로 발생한 예외가 재시도 대상 예외인지 확인한다.
   * */
  private boolean isRetryThrowable(RetryContext context) {
    return retryExceptionList.contains(context.getLastThrowable().getClass());
  }
}
