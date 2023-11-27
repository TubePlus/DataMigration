package com.datamigration.datamigration.rankingaggregation.batch.job;


import com.datamigration.datamigration.rankingaggregation.batch.chunk.QuerydslPagingItemReader;
import com.datamigration.datamigration.rankingaggregation.domain.*;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CommunityInteractionRepository;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CommunityRankRepository;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CreatorDataAggregationRepository;
import com.datamigration.datamigration.rankingaggregation.infrastructure.InteractionDataAggregationRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AggregationJob {

    private static final Integer CHUNK_SIZE = 100;
    private final JobRepository jobRepository;
    private final EntityManagerFactory emf;
    private final PlatformTransactionManager transactionManager;

    private final InteractionDataAggregationRepository interactionDataAggregationRepository;
    private final CreatorDataAggregationRepository creatorDataAggregationRepository;
    private final CommunityInteractionRepository communityInteractionRepository;
    private final CommunityRankRepository communityRankRepository;

//    private final RedisTemplate<String, Object> redisTemplate;

    // JOB 시작시간
    public LocalDateTime activationTime = LocalDateTime.now();

    // JOB
    @Bean
    public Job rankingAggregationJob() {

        return new JobBuilder("rankingAggregationJob", jobRepository)
                .start(rankingAggregationStep())
                .next(rankingCommunityStep())
                .next(cleanUpStep())
                .build();
    }

    /**
     * TASKLET
     */

    // 배치 처리 후 communityInteraction 테이블 데이터 삭제
    public class CleanUpTasklet implements Tasklet {

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

            // 1. 시간별 집계 테이블 데이터 삭제
            communityInteractionRepository.deleteAllByCreatedDateBefore(activationTime);

            return RepeatStatus.FINISHED;
        }
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
                .writer(rankingCommunityWriter())
                .build();
    }

    // 배치 처리 후 communityInteraction 테이블 데이터 삭제
    @Bean
    public Step cleanUpStep() {

        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(new CleanUpTasklet(), transactionManager)
                .build();
    }

    /**
     * READER
     */

    // 시간별 집계 테이블로 데이터 이관
    @Bean
    public QuerydslPagingItemReader<CommunityInteraction> rankingAggregationReader() {

        QCommunityInteraction qCommunityInteraction = QCommunityInteraction.communityInteraction;
        QuerydslPagingItemReader<CommunityInteraction> reader = new QuerydslPagingItemReader<>(
                emf, CHUNK_SIZE, queryFactory -> queryFactory
                .selectFrom(qCommunityInteraction)
                .where(qCommunityInteraction.createdDate.before(activationTime))
        );

        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    // 커뮤니티 랭킹 테이블로 데이터 이관
    @Bean
    public QuerydslPagingItemReader<InteractionDataAggregation> rankingCommunityReader() {

        QInteractionDataAggregation qInteractionDataAggregation = QInteractionDataAggregation.interactionDataAggregation;
        QuerydslPagingItemReader<InteractionDataAggregation> reader = new QuerydslPagingItemReader<>(
                emf, CHUNK_SIZE, queryFactory -> queryFactory
                .selectFrom(qInteractionDataAggregation)
                .orderBy(qInteractionDataAggregation.points.desc())
        );

        log.info("InteractionData: {}", reader);

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
    @Bean
    public ItemProcessor<InteractionDataAggregation, CommunityRank> rankingCommunityProcessor() {

        return data -> {
            log.info("InteractionData: {}", data);

            Long communityId = data.getCommunityId();
            Long points = data.getPoints();
            Optional<CreatorDataAggregation> creatorData =
                    creatorDataAggregationRepository.findByCommunityId(communityId);

            // 크리에이터의 정보 존재 여부에 따라 랭킹 데이터 생성
            return creatorData.map(creatorDataAggregation -> CommunityRank.builder()
                    .communityId(communityId)
                    .points(points)
                    .category(creatorDataAggregation.getCategory())
                    .memberCount(creatorDataAggregation.getMemberCount())
                    .userUuid(creatorDataAggregation.getUserUuid())
                    .profileImage(creatorDataAggregation.getProfileImage())
                    .youtubeHandler(creatorDataAggregation.getYoutubeHandler())
                    .username(creatorDataAggregation.getUsername())
                    .communityName(creatorDataAggregation.getCommunityName())
                    .youtubeName(creatorDataAggregation.getYoutubeName())
                    .communityRank(0)
                    .build()).orElse(null);
        };
    }

    /**
     * WRITER
     */

    // 시간별 집계 테이블로 데이터 이관
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

    // 커뮤니티 랭킹 테이블로 데이터 이관
    @Bean
    public ItemWriter<CommunityRank> rankingCommunityWriter() {

//        ListOperations<String, Object> redisOperations = redisTemplate.opsForList();

        return chunk -> {

            for (CommunityRank communityRank : chunk) {

                log.info("communityRank in chunk: {}", communityRank.toString());

                if(!communityRankRepository.existsByCommunityId(communityRank.getCommunityId())) {
                    communityRankRepository.save(communityRank);
//                    redisOperations.rightPush("communityRank:" + activationTime, communityRank);
                } else {
                    CommunityRank savedCommunityRank =
                            communityRankRepository.findByCommunityId(communityRank.getCommunityId());
                    savedCommunityRank.updateCommunityRank(
                            communityRank.getPoints(), communityRank.getCategory(),
                            communityRank.getMemberCount(), communityRank.getProfileImage(),
                            communityRank.getYoutubeHandler(), communityRank.getUsername(),
                            communityRank.getCommunityName(), communityRank.getYoutubeName());

//                    redisOperations.rightPush("communityRank:" + activationTime, savedCommunityRank);
                }
            }
        };
    }
}
