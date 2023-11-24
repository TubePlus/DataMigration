package com.datamigration.datamigration.rankingaggregation.application;

import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationCommunityRequest;
import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationUserRequest;

public interface CreatorDataAggregationService {
    void saveCommunityInteraction(CreatorDataAggregationUserRequest creatorDataAggregationUserRequest);

    void putCreatorDataAggregationByCommunity(CreatorDataAggregationCommunityRequest creatorDataAggregationCommunityRequest);
    void putCreatorDataAggregationByMemberCount(Long communityId, Integer memberCount);
}
