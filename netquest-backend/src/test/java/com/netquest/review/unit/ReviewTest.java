package com.netquest.review.unit;

import com.netquest.domain.review.model.Review;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class ReviewTest {


    @Test
    void ReviewNullTest(){

        NullPointerException exception = catchThrowableOfType(
                () ->
                        new Review(null, null, null, null,null,null,null),
                NullPointerException.class
        );

        assertThat(exception.getMessage()).isNotEmpty();

    }
}
