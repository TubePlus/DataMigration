package com.datamigration.datamigration.rankingaggregation.infrastructure;


import com.datamigration.datamigration.rankingaggregation.domain.CommunityInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityInteractionRepository extends JpaRepository<CommunityInteraction, Long> {

}
