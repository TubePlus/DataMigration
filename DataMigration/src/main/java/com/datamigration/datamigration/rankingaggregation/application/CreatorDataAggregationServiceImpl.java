package com.datamigration.datamigration.rankingaggregation.application;

import com.datamigration.datamigration.global.error.ErrorCode;
import com.datamigration.datamigration.global.error.handler.BusinessException;
import com.datamigration.datamigration.rankingaggregation.domain.CreatorDataAggregation;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CreatorDataAggregationRepository;
import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationCommunityRequest;
import com.datamigration.datamigration.rankingaggregation.vo.CreatorDataAggregationUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorDataAggregationServiceImpl implements CreatorDataAggregationService{
    private final CreatorDataAggregationRepository creatorDataAggregationRepository;

    @Override
    @Transactional
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
                    .memberCount(0)
                    .profileImage(creatorDataAggregationUserRequest.getProfileImage())
                    .youtubeHandler(creatorDataAggregationUserRequest.getYoutubeHandler())
                    .username(creatorDataAggregationUserRequest.getUserName())
                    .build();
            creatorDataAggregationRepository.save(creatorDataAggregation);
        }
    }

    @Override
    @Transactional
    public void putCreatorDataAggregationByCommunity(CreatorDataAggregationCommunityRequest creatorDataAggregationCommunityRequest) {
        CreatorDataAggregation creatorDataAggregation
                = creatorDataAggregationRepository.findByUserUuid(creatorDataAggregationCommunityRequest.getUserUuid())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));
        creatorDataAggregation.updateCommunity(creatorDataAggregationCommunityRequest);
        creatorDataAggregationRepository.save(creatorDataAggregation);
    }

    @Override
    @Transactional
    public void putCreatorDataAggregationByMemberCount(Long communityId, Integer memberCount) {
        CreatorDataAggregation creatorDataAggregation
                = creatorDataAggregationRepository.findByCommunityId(communityId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE));
        creatorDataAggregation.updateMemberCount(memberCount);
        creatorDataAggregationRepository.save(creatorDataAggregation);
    }
}
