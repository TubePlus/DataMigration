//package com.datamigration.datamigration.batch;
//
//import com.datamigration.datamigration.batch.domain.Aggregation;
//import com.datamigration.datamigration.batch.domain.CommunityInteraction;
//import jakarta.persistence.EntityManagerFactory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import static com.datamigration.datamigration.batch.JobConfig.CHUNK_SIZE;
//
//@RequiredArgsConstructor
//@Configuration
//@Slf4j
//public class WriterConfig {
//
//    private final EntityManagerFactory emf;
//    private final JobConfig jobConfig;
//
//    @Bean
//    public JpaPagingItemReader<CommunityInteraction> rankingItemReader() {
//
//        return new JpaPagingItemReaderBuilder<CommunityInteraction>()
//                .name("rankingItemReader")
//                .entityManagerFactory(emf)
//                .pageSize(CHUNK_SIZE)
//                .queryString("SELECT c FROM CommunityInteraction c")
//                .build();
//    }
//
//    @Bean
//    public ItemWriter<Aggregation> rankingItemWriter() {
//
//        return items -> {
//            for (Aggregation item : items) {
//                System.out.println("items=" + item);
//            }
//        };
//    }
//}
