package com.datamigration.datamigration.rankingaggregation.infrastructure;


import com.datamigration.datamigration.rankingaggregation.domain.CommunityRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRankRepository extends JpaRepository<CommunityRank, Long> {
}
