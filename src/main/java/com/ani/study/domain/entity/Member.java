package com.ani.study.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "member")
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "mem_id")
  private Long id;

  @Column(name = "mem_nm")
  private String memNm;

  @Column(name = "status")
  private String status;

  public void applyMemberStatus(String status) {
    this.status = status;
  }
}
