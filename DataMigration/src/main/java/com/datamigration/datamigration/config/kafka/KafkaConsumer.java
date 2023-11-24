package com.datamigration.datamigration.config.kafka;

import com.datamigration.datamigration.config.kafka.dto.CommunityInteractionDto;

import com.datamigration.datamigration.rankingaggregation.application.CreatorDataAggregationService;
import com.datamigration.datamigration.rankingaggregation.domain.CommunityInteraction;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CommunityInteractionRepository;
import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationCommunityRequest;
import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CommunityInteractionRepository communityInteractionRepository;
    private final CreatorDataAggregationService creatorDataAggregationService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @KafkaListener(topics="communityInteraction", groupId="communityInteraction-dataMigration-service")
    public void processMessage(String kafkaMessage) throws JsonProcessingException {
        CommunityInteractionDto communityInteractionDto
                = objectMapper.readValue(kafkaMessage, CommunityInteractionDto.class);
        communityInteractionRepository.save(
                CommunityInteraction.builder()
                    .communityId(communityInteractionDto.getCommunityId())
                    .point(communityInteractionDto.getPoint())
                    .interactionType(communityInteractionDto.getInteractionType())
                    .build());
        if (communityInteractionDto.getPoint() == 800) {
            creatorDataAggregationService
                    .putCreatorDataAggregationByMemberCount(communityInteractionDto.getCommunityId(),1);
        }
    }
    @KafkaListener(topics="creatorDataAggregation", groupId="creatorDataAggregation-dataMigration-service")
    public void consumeCreatorDataAggregation(String kafkaMessage) throws JsonProcessingException {
        CreatorDataAggregationUserRequest creatorDataAggregationUserRequest
                = objectMapper.readValue(kafkaMessage, CreatorDataAggregationUserRequest.class);
        creatorDataAggregationService.saveCommunityInteraction(creatorDataAggregationUserRequest);
    }

    @KafkaListener(topics="creatorDataAggregationCommunity", groupId="creatorDataAggregationCommunity-dataMigration-service")
    public void consumeCreatorDataAggregationCommunity(String kafkaMessage) throws JsonProcessingException {
        CreatorDataAggregationCommunityRequest creatorDataAggregationUserRequest
                = objectMapper.readValue(kafkaMessage, CreatorDataAggregationCommunityRequest.class);
        creatorDataAggregationService.putCreatorDataAggregationByCommunity(creatorDataAggregationUserRequest);
    }
}
