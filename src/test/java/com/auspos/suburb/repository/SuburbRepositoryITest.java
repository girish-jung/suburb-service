package com.auspos.suburb.repository;

import com.auspos.suburb.domain.Suburb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Stream;

import static com.auspos.suburb.domain.Suburb.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
public class SuburbRepositoryITest {

    @Autowired
    private SuburbRepository suburbRepository;

    @BeforeEach
    public void setup() {
        suburbRepository.save(builder().suburbName("Williams Landing").postCode(3027).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Point Cook").postCode(3030).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Werribee").postCode(3030).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Cremorne").postCode(2090).state("NSW").country("Australia").build());
        suburbRepository.save(builder().suburbName("Hoppers Crossing").postCode(3029).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Cremorne").postCode(3121).state("Victoria").country("Australia").build());
    }

    @AfterEach
    public void tearDown() {
        suburbRepository.deleteAll();
    }

    @Test
    public void findByPostCode_validPostCode_shouldReturnSuburb() {
        assertThat(suburbRepository.findByPostCode(3027).size(), equalTo(1));
        assertThat(suburbRepository.findByPostCode(3029).size(), equalTo(1));
        assertThat(suburbRepository.findByPostCode(3030).size(), equalTo(2));
    }

    @Test
    public void findByPostCode_invalidPostCode_shouldNotReturnSuburb() {
        Assertions.assertTrue(CollectionUtils.isEmpty(suburbRepository.findByPostCode(3000)));
    }

    @Test
    public void findBySuburbNameIgnoreCase_validName_shouldReturnSuburb() {
        List<Suburb> suburbs = suburbRepository.findBySuburbNameIgnoreCase("point cook");
        assertThat(suburbs.size(), equalTo(1));
        assertThat(suburbs.get(0).getSuburbName(), equalTo("Point Cook"));
        assertThat(suburbs.get(0).getPostCode(), equalTo(3030L));
    }

    @Test
    public void findBySuburbNameIgnoreCase_invalidName_shouldNotReturnSuburb() {
        Assertions.assertTrue(CollectionUtils.isEmpty(suburbRepository.findBySuburbNameIgnoreCase("Toorak")));
    }

    @Test
    public void findBySuburbNameAndPostCode_validValues_shouldReturnSuburb() {
        List<Suburb> suburbs = suburbRepository.findBySuburbNameIgnoreCaseAndPostCode("cremorne", 2090);
        assertThat(suburbs.size(), equalTo(1));
        assertThat(suburbs.get(0).getSuburbName(), equalTo("Cremorne"));
        assertThat(suburbs.get(0).getPostCode(), equalTo(2090L));

        suburbs = suburbRepository.findBySuburbNameIgnoreCaseAndPostCode("cremorne", 3121);
        assertThat(suburbs.get(0).getSuburbName(), equalTo("Cremorne"));
        assertThat(suburbs.get(0).getPostCode(), equalTo(3121L));
    }

    @Test
    public void findBySuburbNameAndPostCode_invalidValid_shouldNotReturnSuburb() {
        Assertions.assertTrue(CollectionUtils.isEmpty(suburbRepository.findBySuburbNameIgnoreCaseAndPostCode("Toorak", 1234)));
    }

    @ParameterizedTest
    @MethodSource("sqlInjectionExamples")
    public void findBySuburbNameIgnoreCase_sqlInjection_shouldNotCorruptDb(String name) {
        assertThat(suburbRepository.findAll().size(), equalTo(6));
        List<Suburb> suburbs = suburbRepository.findBySuburbNameIgnoreCase("point cook;DROP Table suburb;");
        Assertions.assertTrue(CollectionUtils.isEmpty(suburbs));
        assertThat(suburbRepository.findAll().size(), equalTo(6));
    }

    private static Stream<Arguments> sqlInjectionExamples() {
        return Stream.of (
                Arguments.of("point cook;DROP Table suburb;"),
                Arguments.of("point cook or 1=1;"),
                Arguments.of("point cook \"\"=\"\"")
        );
    }

}
