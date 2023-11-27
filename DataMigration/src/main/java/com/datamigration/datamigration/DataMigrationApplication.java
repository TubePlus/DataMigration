package com.datamigration.datamigration;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka // kafka 활성화
@EnableJpaAuditing // base entity 자동 적용
@EnableScheduling // 스케줄링 활성화
@SpringBootApplication
@EnableBatchProcessing // 배치 기능 활성화
@EnableCaching // 캐시 활성화
public class DataMigrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataMigrationApplication.class, args);
	}

}
