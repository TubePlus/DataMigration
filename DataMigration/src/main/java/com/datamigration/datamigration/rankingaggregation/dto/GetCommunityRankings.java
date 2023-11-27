package com.datamigration.datamigration.rankingaggregation.dto;

import com.datamigration.datamigration.rankingaggregation.domain.CommunityRank;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "@class"
)
public class GetCommunityRankings {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private List<CommunityRank> communityRankings;
    }
}
