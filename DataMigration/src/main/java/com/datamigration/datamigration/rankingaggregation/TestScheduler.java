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
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class TestScheduler {
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private JobConfig jobConfig;
//
//    // 5초마다 실행
//    @Scheduled(fixedDelay = 5000)
//    public void runJob() throws Exception {
//        System.out.println("QuerydslPagingItemReader");
//        //job parameter 설정
//        Map<String, JobParameter<?>> confMap = new HashMap<>();
//
//        Calendar calendar = Calendar.getInstance();
//        Date date = calendar.getTime();
//        confMap.put("time", new JobParameter<>(date, Date.class, true));
//        JobParameters jobParameters = new JobParameters(confMap);
//
//        try {
//            jobLauncher.run(jobConfig.rankAggregationJob(), jobParameters);
//        } catch (Exception e) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            System.out.println(String.format("ERROR TIME : %s", sdf.format(date)));;
//            log.error(e.getMessage(), e);
//        }
//    }
//}
