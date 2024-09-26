package com.ani.study.domain.service;

import com.ani.study.domain.entity.Member;
import com.ani.study.domain.repository.MemberJPARepository;
import com.ani.study.exception.TestCusotmException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {
  private final MemberJPARepository memberJPARepository;

  public void memberUpdate(Map<String, Integer> map) {
    try {
      map.forEach((key, value) -> {
        Long id = Long.parseLong(String.valueOf(value));

        Member member = memberJPARepository.findById(id)
            .orElseThrow(RuntimeException::new);

        String status = "WITHDRAW";

        member.applyMemberStatus(status);

        memberJPARepository.save(member);

        log.info("Member >>>> memId : {} status : {}", member.getId(), member.getStatus());
        if(id.equals(3L)) {
          throw new TestCusotmException();
        }
      });
    } catch (TestCusotmException e) {
      log.error("커스텀 에러 발생", e);
      throw e;
    } catch (Exception e) {
      log.error("기타에러", e);
      throw e;
    }
  }
}
