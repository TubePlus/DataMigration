package com.datamigration.datamigration.rankingaggregation.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableCaching
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job rankingAggregationJob;
    private final PlatformTransactionManager transactionManager;

    // 매일 10시, 15시, 20시에 실행
    @Scheduled(cron = "0 0 10,15,20 * * *")
    public void runJob() throws Exception {

        // jobParams : job을 실행할때 넘겨주고싶은 파라미터 & job을 고유하게 식별하는 역할
        // 중복방지 : jobParams에 addDate만 남겨두면, 키는 일정하고 params도 하루동안은 일정하기에 중복 실행을 방지할 수 있음
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        log.info(today.toString());

        JobParameters jobParams = new JobParametersBuilder()
                .addDate("date", today)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        log.info(jobParams.toString());

        // transactionTemplate : 트랜잭션을 직접 컨트롤할 수 있게 해주는 스프링의 유틸리티 클래스
//        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
//        transactionTemplate.executeWithoutResult(status -> {

            try {
                JobExecution jobExecution = jobLauncher.run(rankingAggregationJob, jobParams);
                jobExecution.getStatus();

            } catch (Exception e) {
                log.error("JobLauncher error: {}", e.getMessage());
//                status.setRollbackOnly();
            }
//        });
    }
}
