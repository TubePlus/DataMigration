package com.datamigration.datamigration.rankingaggregation.domain;

import com.datamigration.datamigration.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityInteraction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_id")
    private Long communityId;

    @Column(name = "point")
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type")
    private InteractionType interactionType;
}