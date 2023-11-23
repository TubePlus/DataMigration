package com.datamigration.datamigration.config.kafka;

import com.datamigration.datamigration.config.kafka.dto.CommunityInteractionDto;

import com.datamigration.datamigration.rankingaggregation.domain.CommunityInteraction;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CommunityInteractionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CommunityInteractionRepository communityInteractionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @KafkaListener(topics="communityInteraction", groupId="communityInteraction-dataMigration-service")
    public void processMessage(String kafkaMessage) throws JsonProcessingException {
        log.info("kafka message received =====> " + kafkaMessage);
        CommunityInteractionDto communityInteractionDto
                = objectMapper.readValue(kafkaMessage, CommunityInteractionDto.class);
        log.info("kafka dto message received =====> " + communityInteractionDto);
        communityInteractionRepository.save(
                CommunityInteraction.builder()
                    .communityId(communityInteractionDto.getCommunityId())
                    .point(communityInteractionDto.getPoint())
                    .interactionType(communityInteractionDto.getInteractionType())
                    .build());
    }

}
