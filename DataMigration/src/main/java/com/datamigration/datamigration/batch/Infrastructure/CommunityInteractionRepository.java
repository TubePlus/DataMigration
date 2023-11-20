package com.datamigration.datamigration.batch.Infrastructure;

import com.datamigration.datamigration.batch.domain.CommunityInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CommunityInteractionRepository extends JpaRepository<CommunityInteraction, Long> {

    void saveList(List<CommunityInteraction> communityInteractions);
}
