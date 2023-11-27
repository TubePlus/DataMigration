package com.datamigration.datamigration.rankingaggregation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "community_rank")
public class CommunityRank {

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("community_id")
    @Column(name = "community_id")
    private Long communityId;

    @JsonProperty("points")
    @Column(name = "points")
    private Long points;

    @JsonProperty("user_uuid")
    @Column(name = "user_uuid")
    private String userUuid;

    @JsonProperty("community_rank")
    @Column(name = "community_rank")
    private Integer communityRank;

    @JsonProperty("category")
    @Column(name = "category")
    private String category;

    @JsonProperty("member_count")
    @Column(name = "member_count")
    private Integer memberCount;

    @JsonProperty("profile_image")
    @Column(name = "profile_image")
    private String profileImage;

    @JsonProperty("youtube_handler")
    @Column(name = "youtube_handler")
    private String youtubeHandler;

    @JsonProperty("username")
    @Column(name = "username")
    private String username;

    @JsonProperty("community_name")
    @Column(name = "community_name")
    private String communityName;

    @JsonProperty("youtube_name")
    @Column(name = "youtube_name")
    private String youtubeName;

    public void updateCommunityRank(
            Long points, String category, Integer memberCount, String profileImage,
            String youtubeHandler, String username, String communityName, String youtubeName) {

        this.points = points;
        this.category = category;
        this.memberCount = memberCount;
        this.profileImage = profileImage;
        this.youtubeHandler = youtubeHandler;
        this.username = username;
        this.communityName = communityName;
        this.youtubeName = youtubeName;
    }
}