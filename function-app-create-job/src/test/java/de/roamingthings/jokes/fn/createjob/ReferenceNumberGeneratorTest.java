package de.roamingthings.jokes.fn.createjob;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class ReferenceNumberGeneratorTest {

    @Test
    void should_generate_different_numbers() {
        ReferenceNumberGenerator generator = new ReferenceNumberGenerator();

        var reference1 = generator.generateReferenceNumber();
        var reference2 = generator.generateReferenceNumber();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(reference1).isNotBlank();
            softly.assertThat(reference2).isNotBlank();
            softly.assertThat(reference1).isNotEqualTo(reference2);
        });
    }
}
