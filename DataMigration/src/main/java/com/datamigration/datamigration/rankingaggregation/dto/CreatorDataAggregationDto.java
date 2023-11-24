package com.datamigration.datamigration.rankingaggregation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreatorDataAggregationDto {
    private Long communityId;
    private String userUuid;
    private String category;
    private Integer memberCount;
    private String profileImage;
    private String youtubeHandler;
    private String username;
    private String communityName;
    private String youtubeName;
}
