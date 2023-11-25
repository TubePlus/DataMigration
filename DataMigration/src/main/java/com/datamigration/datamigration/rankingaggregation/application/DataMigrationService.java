package com.datamigration.datamigration.rankingaggregation.application;


import com.datamigration.datamigration.rankingaggregation.domain.CommunityRank;

import java.util.List;

public interface DataMigrationService {

    List<CommunityRank> getCommunityRankings(Integer size);
}