//package com.datamigration.datamigration.rankingaggregation;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.rankingaggregation.core.JobParameter;
//import org.springframework.rankingaggregation.core.JobParameters;
//import org.springframework.rankingaggregation.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class BatchScheduler {
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private JobConfig jobConfig;
//
//    @Scheduled(cron = "0 0 0 * * *")
//    private void runJob() {
//
//        // job parameter 설정
//        Map<String, JobParameter> jobParameterMap = new HashMap<>();
//        jobParameterMap.put("time", new JobParameter(System.currentTimeMillis()));
//        JobParameters jobParameters = new JobParameters(jobParameterMap);
//    }
//}
