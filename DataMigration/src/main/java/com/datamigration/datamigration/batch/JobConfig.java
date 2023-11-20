package com.datamigration.datamigration.batch;

import com.datamigration.datamigration.batch.domain.Aggregation;
import com.datamigration.datamigration.batch.domain.CommunityInteraction;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class JobConfig {

    private static final Integer CHUNK_SIZE = 100;
    private final EntityManagerFactory emf;
    private final AggregatedItemWriter aggregatedItemWriter;

    @Bean
    public Job rankAggregationJob(
            JobRepository jobRepository, Step rankAggregationStep, Step rankAggregationStopStep) {

        return new JobBuilder("creatorRankingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(rankAggregationStep)
                .build();
    }

    @Bean
    @JobScope // Step 선언문에서 사용
    public Step rankAggregationStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) throws Exception {

        // 새로운 스텝 생성, 스텝 이름 설정, 트랜잭션 관리자 설정
        return new StepBuilder("rankAggregationStep", jobRepository)
                // 해당 스텝에서 처리할 아이템 타입을 CommunityInteraction에서 Aggregation으로 변경
                .<List<CommunityInteraction>, List<Aggregation>>chunk(CHUNK_SIZE, transactionManager)
                .reader(rankingItemReader())
                .processor(rankingItemProcessor())
                .writer(rankingItemWriter())
//                .taskExecutor(taskExecutor) // 각 스레드 chunk 단위로 실행(멀티 스레딩)
                .build();

    }

    // READER
    // Chunk 단위로 트랜잭션 처리
    @StepScope
    @Bean
    public JpaPagingItemReader<List<CommunityInteraction>> rankingItemReader() {

        // jpa 사용하여 데이터를 페이지 별로 읽어옴
        JpaPagingItemReader<List<CommunityInteraction>> reader = new JpaPagingItemReader<>();
        reader.setName("rankingItemReader");
        reader.setEntityManagerFactory(emf);
        // 한번에 읽어올 양 설정
        reader.setPageSize(CHUNK_SIZE);
        reader.setQueryString("SELECT data FROM CommunityInteraction c");

        log.info("reader={}", reader);
        return reader;
    }

    @StepScope
    @Bean
    public ItemProcessor<List<CommunityInteraction>, List<Aggregation>> rankingItemProcessor() throws Exception {

        HashMap<Long, Long> processorMap = new HashMap<>();

        for(CommunityInteraction interaction : Objects.requireNonNull(rankingItemReader().read())) {

            log.info("interaction={}", interaction);

            processorMap.put(
                    interaction.getCommunityId(),
                    processorMap.getOrDefault(
                            interaction.getCommunityId(), 0L) + interaction.getPoint()
            );

            log.info("processorMap={}", processorMap);
        }

        return null;
    }

    @StepScope
    @Bean
    public JpaItemWriter<List<Aggregation>> rankingItemWriter() {

        JpaItemWriter<List<Aggregation>> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    public class ItemListProcessor implements ItemProcessor<CommunityInteraction, List<Aggregation>> {

        @Override
        public List<Aggregation> process(CommunityInteraction item) throws Exception {

            return List.of(
                    new Aggregation((long) 100, (long) 1, 1L)
            );
        }
    }
}
