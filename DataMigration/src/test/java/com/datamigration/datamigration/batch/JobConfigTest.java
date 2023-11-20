package com.datamigration.datamigration.batch;

import com.datamigration.datamigration.batch.Infrastructure.AggregationRepository;
import com.datamigration.datamigration.batch.Infrastructure.CommunityInteractionRepository;
import com.datamigration.datamigration.batch.domain.CommunityInteraction;
import com.datamigration.datamigration.batch.domain.InteractionType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JobConfigTest {

    @Autowired
    private AggregationRepository aggregationRepository;
    @Autowired
    private CommunityInteractionRepository communityInteractionRepository;
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void 테스트() throws Exception {

        //given
        communityInteractionRepository.saveList(
                Arrays.asList(
                        new CommunityInteraction(1L, 1L, -1, InteractionType.COMMENT),
                        new CommunityInteraction(2L, 1L, 1, InteractionType.LIKE),
                        new CommunityInteraction(3L, 1L, 1, InteractionType.JOIN),
                        new CommunityInteraction(4L, 1L, 1, InteractionType.JOIN),
                        new CommunityInteraction(5L, 1L, 1, InteractionType.JOIN),
                        new CommunityInteraction(6L, 1L, 1, InteractionType.JOIN),
                        new CommunityInteraction(7L, 1L, 1, InteractionType.JOIN),
                        new CommunityInteraction(8L, 1L, 1, InteractionType.JOIN),
                        new CommunityInteraction(9L, 1L, 1, InteractionType.JOIN),
                        new CommunityInteraction(10L, 1L, 1, InteractionType.JOIN)
                )
        );

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        //when
        //then
//        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
        assertThat(aggregationRepository.findAll().size()).isEqualTo(10);

    }
}