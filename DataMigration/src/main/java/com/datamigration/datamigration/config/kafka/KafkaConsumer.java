package com.datamigration.datamigration.config.kafka;

import com.datamigration.datamigration.batch.Infrastructure.CommunityInteractionRepository;
import com.datamigration.datamigration.batch.domain.CommunityInteraction;
import com.datamigration.datamigration.config.kafka.dto.CommunityInteractionDto;

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
        Map<Object, Object> map = new HashMap<>(); // kafka 역직렬화
        CommunityInteractionDto communityInteractionDto
                = objectMapper.readValue(kafkaMessage, CommunityInteractionDto.class);
        communityInteractionRepository.save(
                CommunityInteraction.builder()
                    .communityId(communityInteractionDto.getCommunityId())
                    .point(communityInteractionDto.getPoint())
                    .interactionType(communityInteractionDto.getInteractionType())
                    .build());
    }

}
