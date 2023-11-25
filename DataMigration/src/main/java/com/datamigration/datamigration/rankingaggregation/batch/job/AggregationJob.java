package com.datamigration.datamigration.rankingaggregation.batch.job;


import com.datamigration.datamigration.rankingaggregation.batch.chunk.QuerydslPagingItemReader;
import com.datamigration.datamigration.rankingaggregation.domain.*;
import com.datamigration.datamigration.rankingaggregation.infrastructure.InteractionDataAggregationRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AggregationJob {

    private static final Integer CHUNK_SIZE = 100;
    private final JobRepository jobRepository;
    private final EntityManagerFactory emf;
    private final PlatformTransactionManager transactionManager;
    private final InteractionDataAggregationRepository interactionDataAggregationRepository;

    // JOB
    @Bean
    public Job rankingAggregationJob() {

        return new JobBuilder("rankingAggregationJob", jobRepository)
                .start(rankingAggregationStep())
                .next(rankingCommunityStep())
                .build();
    }

    /**
     * STEP
     */

    // 시간별 집계 테이블로 데이터 이관
    @Bean
    public Step rankingAggregationStep() {

        return new StepBuilder("rankingAggregationStep", jobRepository)
                .<CommunityInteraction, InteractionDataAggregation>chunk(CHUNK_SIZE, transactionManager)
                .reader(rankingAggregationReader())
                .processor(rankingAggregationProcessor())
                .writer(rankingAggregationWriter())
                .build();
    }

    // 커뮤니티 랭킹 테이블로 데이터 이관
    @Bean
    public Step rankingCommunityStep() {

        return new StepBuilder("rankingCommunityStep", jobRepository)
                .<InteractionDataAggregation, CommunityRank>chunk(CHUNK_SIZE, transactionManager)
                .reader(rankingCommunityReader())
                .processor(rankingCommunityProcessor())
//                .writer()
                .build();
    }

    /**
     * READER
     */

    // 시간별 집계 테이블로 데이터 이관
    @Bean
    public QuerydslPagingItemReader<CommunityInteraction> rankingAggregationReader() {

        LocalDateTime now = LocalDateTime.now();

        QCommunityInteraction qCommunityInteraction = QCommunityInteraction.communityInteraction;
        QuerydslPagingItemReader<CommunityInteraction> reader = new QuerydslPagingItemReader<>(
                emf, CHUNK_SIZE, queryFactory -> queryFactory
                .selectFrom(qCommunityInteraction)
                .where(qCommunityInteraction.createdDate.before(now))
        );

        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    // 커뮤니티 랭킹 테이블로 데이터 이관
    @Bean
    public QuerydslPagingItemReader<InteractionDataAggregation> rankingCommunityReader() {

        LocalDateTime now = LocalDateTime.now();

        QInteractionDataAggregation qInteractionDataAggregation = QInteractionDataAggregation.interactionDataAggregation;
        QuerydslPagingItemReader<InteractionDataAggregation> reader = new QuerydslPagingItemReader<>(
                emf, CHUNK_SIZE, queryFactory -> queryFactory
                .selectFrom(qInteractionDataAggregation)
        );

        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    /**
     * PROCESSOR
     */

    // 시간별 집계 테이블로 데이터 이관
    @Bean
    public ItemProcessor<CommunityInteraction, InteractionDataAggregation> rankingAggregationProcessor() {

        return interaction -> {
            Long communityId = interaction.getCommunityId();
            Long point = interaction.getPoint();

            return InteractionDataAggregation.builder()
                    .communityId(communityId)
                    .points(point)
                    .build();
        };
    }

    // 커뮤니티 랭킹 테이블로 데이터 이관
    //todo: 마무리하기
    @Bean
    public ItemProcessor<InteractionDataAggregation, CommunityRank> rankingCommunityProcessor() {

        return data -> {
            Long communityId = data.getCommunityId();
            Long points = data.getPoints();

            return CommunityRank.builder().build();
        };
    }

    // WRITER
    @Bean
    public ItemWriter<InteractionDataAggregation> rankingAggregationWriter() {

        return chunk -> {

            for (InteractionDataAggregation interactionDataAggregation : chunk) {

                if(!interactionDataAggregationRepository.existsByCommunityId(interactionDataAggregation.getCommunityId())) {
                    interactionDataAggregationRepository.save(interactionDataAggregation);
                } else {
                    InteractionDataAggregation savedInteractionDataAggregation =
                            interactionDataAggregationRepository.findByCommunityId(interactionDataAggregation.getCommunityId());
                    savedInteractionDataAggregation.updatePoints(interactionDataAggregation.getPoints());
                }
            }
        };
    }

    @Bean
    public ItemWriter<CommunityRank> rankingCommunityWriter() {

        // todo: 저장시에 redis에도 저장해야함.
        return chunk -> {

            for (CommunityRank communityRank : chunk) {


            }

        };
    }
}
