package com.datamigration.datamigration.batch.Infrastructure;

import com.datamigration.datamigration.batch.domain.Aggregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregationRepository extends JpaRepository<Aggregation, Long> {


}
