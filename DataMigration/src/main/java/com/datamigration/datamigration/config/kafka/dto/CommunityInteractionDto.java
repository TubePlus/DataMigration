package com.datamigration.datamigration.config.kafka.dto;

import com.datamigration.datamigration.batch.domain.InteractionType;
import lombok.Getter;

@Getter
public class CommunityInteractionDto {
    private Long communityId;
    private Long point;
    private InteractionType interactionType;
}
