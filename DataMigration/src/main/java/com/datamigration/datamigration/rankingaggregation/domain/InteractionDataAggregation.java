package com.datamigration.datamigration.rankingaggregation.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class InteractionDataAggregation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_id", unique = true)
    private Long communityId;

    @Column(name = "points")
    private Long points;

    public void updatePoints(Long point) {
        this.points += point;
    }
}
