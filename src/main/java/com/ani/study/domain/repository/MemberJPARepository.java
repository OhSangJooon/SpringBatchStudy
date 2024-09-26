package com.ani.study.domain.repository;

import com.ani.study.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJPARepository extends JpaRepository<Member, Long> {

}
