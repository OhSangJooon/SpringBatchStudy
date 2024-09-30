package com.ani.study.common;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryTemplateProvider {

    public RetryTemplate createRetryTemplate(int maxAttempts, Long backOffPeriod
        , List<Class<? extends Throwable>> retryableExceptionList) {
        RetryTemplate retryTemplate = new RetryTemplate();

        // 특정 예외에 대해서만 재시도하도록 설정
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = retryableExceptionList.stream()
            .collect(Collectors.toMap(
                e -> e,
                b -> true
            ));

        // Retry 정책 생성
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts, retryableExceptions);
        retryTemplate.setRetryPolicy(retryPolicy);

        // 재시작 주기 설정
        if(backOffPeriod != null) {
            CustomBackOffPolicy backOffPolicy = new CustomBackOffPolicy(backOffPeriod);
            retryTemplate.setBackOffPolicy(backOffPolicy);
        }

        return retryTemplate;
    }
}
