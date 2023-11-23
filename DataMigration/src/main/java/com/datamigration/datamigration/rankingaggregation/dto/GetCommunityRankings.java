package com.datamigration.datamigration.rankingaggregation.dto;

import com.datamigration.datamigration.rankingaggregation.domain.CommunityRank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class GetCommunityRankings {

    // todo: 안쓰면 삭제하기
    @Getter
    @Builder
    public static class Response {

        private List<CommunityRank> communityRankings;

        Response formResponse(List<CommunityRank> communityRankings) {

            return Response.builder()
                    .communityRankings(communityRankings)
                    .build();
        }
    }
}
