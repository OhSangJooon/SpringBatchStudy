package com.ani.study.common;

import lombok.NoArgsConstructor;
import org.springframework.retry.backoff.FixedBackOffPolicy;

@NoArgsConstructor
public class CustomBackOffPolicy extends FixedBackOffPolicy {
    /**
     * FixedBackOffPolicy 의 period는 생성자로 설정이 불가능 하기 때문에 상속을 받아 생성한다.
     * 생성자로 객체를 생성시 period를 파라미터로 받을 수 있으며 설정하고자 하는 시간을 설정 할 수 있다.
     * Ex) 60000 은 60초 = 1분
     * default 는 1초로 설정된다.
    */
    public CustomBackOffPolicy(long period) {
        this.setBackOffPeriod(period);
    }

}
