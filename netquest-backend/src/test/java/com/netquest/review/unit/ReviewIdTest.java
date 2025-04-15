package com.netquest.review.unit;

import com.netquest.domain.review.model.ReviewId;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewIdTest {

    @Test
    void ReviewIdRandomGenerateTest() {
        int numInstances = 100;
        Set<ReviewId> ids = new HashSet<>();

        for (int i = 0; i < numInstances; i++) {
            ReviewId reviewId = new ReviewId();
            ids.add(reviewId);
        }

        // Check that all IDs are unique by comparing the set size to the number of instances
        assertThat(ids).hasSize(numInstances);
    }
}
