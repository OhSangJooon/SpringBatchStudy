package com.ani.study.controller;

import com.ani.study.domain.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class BatchJobController {

  private final JobService jobService;

  @GetMapping("/start")
  public String startJob(@RequestParam(name = "jobName") String jobName) throws Exception {
    log.debug("\n\n### JobController.startJob \n\n.");
    jobService.invokeJob(jobName);
    return "Job Started...";
  }
}
