package com.datamigration.datamigration.rankingaggregation.batch.launcher;


import com.datamigration.datamigration.global.base.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
@Slf4j
public class AggregationJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job rankingAggregationJob;

    @Tag(name = "데이터 반환")
    @Operation(summary = "데이터 정산처리")
    @GetMapping("/aggregation")
    public ApiResponse<Object> aggregationJobLauncher() {

        // jobParams : job을 실행할때 넘겨주고싶은 파라미터 & job을 고유하게 식별하는 역할
        // 중복방지 : jobParams에 addDate만 남겨두면, 키는 일정하고 params도 하루동안은 일정하기에 중복 실행을 방지할 수 있음
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        log.info(today.toString());

        JobParameters jobParams = new JobParametersBuilder()
                .addDate("date", today)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        log.info(jobParams.toString());

        try {
            JobExecution jobExecution = jobLauncher.run(rankingAggregationJob, jobParams);
            jobExecution.getStatus();
        } catch (Exception e) {
            log.error("JobLauncher error: {}", e.getMessage());
        }

        return ApiResponse.ofSuccess();
    }
}