package com.datamigration.datamigration.rankingaggregation.batch.launcher;


import com.datamigration.datamigration.rankingaggregation.domain.CommunityInteraction;
import com.datamigration.datamigration.rankingaggregation.domain.InteractionType;
import com.datamigration.datamigration.rankingaggregation.infrastructure.CommunityInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@EnableBatchProcessing
public class TestControllerToBeDeleted {

    private final CommunityInteractionRepository communityInteractionRepository;

    @GetMapping("")
    public void test() {

        ArrayList<InteractionType> enumLists = new ArrayList<>();
        enumLists.add(InteractionType.LIKE);
        enumLists.add(InteractionType.JOIN);
        enumLists.add(InteractionType.COMMENT);

        for(int i = 0; i < 50; i++) {

            Random random = new Random();
            Long randomCommunityId = random.nextLong(1, 11);
            Long randomPoint = random.nextLong(1, 101);
            int randomEnumIndex = random.nextInt(0, 3);


            CommunityInteraction communityInteraction = CommunityInteraction.builder()
                    .communityId(randomCommunityId)
                    .point(randomPoint)
                    .interactionType(enumLists.get(randomEnumIndex))
                    .build();
            communityInteractionRepository.save(communityInteraction);
        }
    }
}
