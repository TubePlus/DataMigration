package com.datamigration.datamigration.batch.domain;

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
public class CommunityInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_id")
    private Long communityId;

    @Column(name = "point")
    private Long point;

    @Column(name = "interaction_type")
    private InteractionType interactionType;
}
