package com.datamigration.datamigration.rankingaggregation.infrastructure;

import com.datamigration.datamigration.rankingaggregation.domain.CreatorDataAggregation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorDataAggregationRepository extends JpaRepository<CreatorDataAggregation,Long> {

    void deleteAllByUserUuid(String userUuid);

    boolean existsByUserUuid(String userUuid);

    Optional<CreatorDataAggregation> findByUserUuid(String userUuid);
}
