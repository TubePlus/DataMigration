//package com.datamigration.datamigration.batch;
//
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.Test;
//import org.springframework.batch.core.BatchStatus;
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.test.JobLauncherTestUtils;
//import org.springframework.batch.test.JobRepositoryTestUtils;
//import org.springframework.batch.test.context.SpringBatchTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//
//@SpringBatchTest
//@ContextConfiguration(classes = {JobConfig.class})
//@SpringBootTest(classes = {JobConfig.class, TestBatchConfig.class})
//class JobConfigTest {
//
//    @Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;
//    @Test
//    public void 테스트() throws Exception {
//
//        // given
//        JobParameters jobParameters =
//                jobLauncherTestUtils.getUniqueJobParameters();
//
//        // when
//        JobExecution jobExecution =
//                jobLauncherTestUtils.launchJob(jobParameters);
//
//        // then
//        Assert.assertEquals(ExitStatus.COMPLETED,
//                jobExecution.getExitStatus());
//    }
//}