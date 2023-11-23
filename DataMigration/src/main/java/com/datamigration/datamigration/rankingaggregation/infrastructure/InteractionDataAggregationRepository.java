package com.datamigration.datamigration.rankingaggregation.infrastructure;


import com.datamigration.datamigration.rankingaggregation.domain.InteractionDataAggregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractionDataAggregationRepository extends JpaRepository<InteractionDataAggregation, Long> {

    Boolean existsByCommunityId(Long communityId);
    InteractionDataAggregation findByCommunityId(Long communityId);
}
