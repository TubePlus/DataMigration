package com.datamigration.datamigration.batch;

import com.datamigration.datamigration.batch.Infrastructure.AggregationRepository;
import com.datamigration.datamigration.batch.domain.Aggregation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class AggregatedItemWriter implements ItemWriter<Aggregation> {

//    private final AggregationRepository aggregationRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void write(Chunk<? extends Aggregation> chunk) throws Exception {

        for (Aggregation aggregation : chunk.getItems()) {

            em.persist(aggregation);
        }

        em.flush();
    }
}
