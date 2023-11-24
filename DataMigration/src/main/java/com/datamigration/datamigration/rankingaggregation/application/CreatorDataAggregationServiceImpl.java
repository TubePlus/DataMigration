package com.datamigration.datamigration.rankingaggregation.application;

import com.datamigration.datamigration.global.error.ErrorCode;
import com.datamigration.datamigration.global.error.handler.BusinessException;
import com.datamigration.datamigration.rankingaggregation.domain.CreatorDataAggregation;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CreatorDataAggregationRepository;
import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationCommunityRequest;
import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorDataAggregationServiceImpl implements CreatorDataAggregationService{
    private final CreatorDataAggregationRepository creatorDataAggregationRepository;

    @Override
    public void saveCommunityInteraction(CreatorDataAggregationUserRequest creatorDataAggregationUserRequest) {
        if (creatorDataAggregationRepository.existsByUserUuid(creatorDataAggregationUserRequest.getUserUuid())) {
            // 데이터 수정하기
            CreatorDataAggregation creatorDataAggregation = creatorDataAggregationRepository.findByUserUuid(creatorDataAggregationUserRequest.getUserUuid())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
            creatorDataAggregation.updateUser(creatorDataAggregationUserRequest);
            creatorDataAggregationRepository.save(creatorDataAggregation);
            return;
        } else {
            // 데이터 생성하기
            CreatorDataAggregation creatorDataAggregation = CreatorDataAggregation.builder()
                    .userUuid(creatorDataAggregationUserRequest.getUserUuid())
                    .category(creatorDataAggregationUserRequest.getCategory())
                    .profileImage(creatorDataAggregationUserRequest.getProfileImage())
                    .youtubeHandler(creatorDataAggregationUserRequest.getYoutubeHandler())
                    .username(creatorDataAggregationUserRequest.getUserName())
                    .build();
            creatorDataAggregationRepository.save(creatorDataAggregation);
        }
    }

    @Override
    public void putCreatorDataAggregationByCommunity(CreatorDataAggregationCommunityRequest creatorDataAggregationCommunityRequest) {
        CreatorDataAggregation creatorDataAggregation
                = creatorDataAggregationRepository.findByUserUuid(creatorDataAggregationCommunityRequest.getUserUuid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));
        creatorDataAggregation.updateCommunity(creatorDataAggregationCommunityRequest);
        creatorDataAggregationRepository.save(creatorDataAggregation);
    }
}
