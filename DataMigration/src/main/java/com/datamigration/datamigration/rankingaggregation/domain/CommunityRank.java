package com.datamigration.datamigration.rankingaggregation.domain;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_id")
    private Long communityId;

    @Column(name = "points")
    private Long points;

    @Column(name = "community_rank")
    private Long communityRank;

    @Column(name = "category")
    private String category;

    @Column(name = "member_count")
    private Integer memberCount;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "youtube_handler")
    private String youtubeHandler;

    @Column(name = "username")
    private String username;

    @Column(name = "community_name")
    private String communityName;

    @Column(name = "youtube_name")
    private String youtubeName;
}