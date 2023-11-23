package com.datamigration.datamigration.rankingaggregation.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class CreatorDataAggregation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_id")
    private Long communityId;

    @Column(name = "user_uuid")
    private String userUuid;

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
