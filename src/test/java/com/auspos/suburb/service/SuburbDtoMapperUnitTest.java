package com.auspos.suburb.service;

import com.auspos.suburb.domain.Suburb;
import com.auspos.suburb.dto.SuburbDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.auspos.suburb.domain.Suburb.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SuburbDtoMapperUnitTest {

    private final SuburbDtoMapper suburbDtoMapper = new SuburbDtoMapper();

    @Test
    public void mapDomainToDto_shouldConvert() {
        Suburb suburb = Suburb.builder()
                .id(1)
                .suburbName("Williams Landing")
                .postCode(3027)
                .state("Victoria")
                .country("Australia")
                .build();

        SuburbDto suburbDto = suburbDtoMapper.mapDomainToDto(suburb);

        assertThat(suburb.getId(), equalTo(suburbDto.getId()));
        assertThat(suburb.getSuburbName(), equalTo(suburbDto.getSuburbName()));
        assertThat(suburb.getPostCode(), equalTo(suburbDto.getPostCode()));
        assertThat(suburb.getState(), equalTo(suburbDto.getState()));
        assertThat(suburb.getCountry(), equalTo(suburbDto.getCountry()));
    }

    @Test
    public void mapDtoToDomain_shouldConvert() {
        SuburbDto suburbDto = SuburbDto.builder()
                .id(1L)
                .suburbName("Williams Landing")
                .postCode(3027L)
                .state("Victoria")
                .country("Australia")
                .build();

        Suburb suburb = suburbDtoMapper.mapDtoToDomain(suburbDto);

        assertThat(suburbDto.getSuburbName(), equalTo(suburb.getSuburbName()));
        assertThat(suburbDto.getPostCode(), equalTo(suburb.getPostCode()));
        assertThat(suburbDto.getState(), equalTo(suburb.getState()));
        assertThat(suburbDto.getCountry(), equalTo(suburb.getCountry()));
    }

    @Test
    public void mapDomainToDtoList_shouldConvert() {
        List<Suburb> suburbs = Arrays.asList(
                builder().id(1).suburbName("Williams Landing").postCode(3027).state("Victoria").country("Australia").build(),
                builder().id(2).suburbName("Hoppers Crossing").postCode(3029).state("Victoria").country("Australia").build(),
                builder().id(3).suburbName("Point Cook").postCode(3030).state("Victoria").country("Australia").build(),
                builder().id(4).suburbName("Werribee").postCode(3030).state("Victoria").country("Australia").build()
        );

        List<SuburbDto> suburbDtos = suburbDtoMapper.mapDomainToDto(suburbs);
        assertThat(suburbDtos.size(), equalTo(suburbs.size()));
    }
}
