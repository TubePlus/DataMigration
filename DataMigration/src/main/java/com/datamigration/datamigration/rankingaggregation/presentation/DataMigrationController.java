package com.datamigration.datamigration.rankingaggregation.presentation;

import com.datamigration.datamigration.rankingaggregation.application.DataMigrationService;
import com.datamigration.datamigration.rankingaggregation.domain.CommunityRank;
import com.datamigration.datamigration.global.base.ApiResponse;
import com.datamigration.datamigration.rankingaggregation.dto.GetCommunityRankings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data-migration")
@RequiredArgsConstructor
public class DataMigrationController {

    private final DataMigrationService dataMigrationService;

    @Tag(name = "데이터 반환")
    @Operation(summary = "정산 끝난 랭킹 데이터 반환")
    @GetMapping("/community-rankings/{size}")
    @Cacheable(value = "communityRankings",
            key = "T(java.time.LocalDateTime).now().withMinute(0).withSecond(0).withNano(0).toString()",
            cacheManager = "redisCacheManager")
    public ApiResponse<GetCommunityRankings.Response> getCommunityRankings(@PathVariable Integer size) {

        List<CommunityRank> rankingData = dataMigrationService.getCommunityRankings(size);

        GetCommunityRankings.Response response = GetCommunityRankings.Response.builder()
                .communityRankings(rankingData)
                .build();

        return ApiResponse.ofSuccess(response);
    }
}
