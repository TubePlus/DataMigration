package com.datamigration.datamigration.rankingaggregation.application;

import com.datamigration.datamigration.rankingaggregation.domain.CommunityRank;
import com.datamigration.datamigration.rankingaggregation.domain.QCommunityRank;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CommunityRankRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataMigrationServiceImpl implements DataMigrationService {

    private final CommunityRankRepository communityRankRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommunityRank> getCommunityRankings(Integer size) {

        QCommunityRank qCommunityRank = new QCommunityRank("communityRank");

        return queryFactory
                .selectFrom(qCommunityRank)
                .orderBy(qCommunityRank.communityRank.asc())
                .limit(size)
                .fetch();
    }
}
