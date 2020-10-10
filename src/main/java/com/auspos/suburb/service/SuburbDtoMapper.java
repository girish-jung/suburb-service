package com.auspos.suburb.service;

import com.auspos.suburb.domain.Suburb;
import com.auspos.suburb.dto.SuburbDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SuburbDtoMapper {

    public List<SuburbDto> mapDomainToDto(List<Suburb> suburbs) {
        return suburbs.stream().map(suburb -> mapDomainToDto(suburb)).collect(Collectors.toList());
    }

    public SuburbDto mapDomainToDto(Suburb suburb) {
        return SuburbDto.builder()
                .id(suburb.getId())
                .suburbName(suburb.getSuburbName())
                .postCode(suburb.getPostCode())
                .state(suburb.getState())
                .country(suburb.getCountry())
                .build();
    }

    public Suburb mapDtoToDomain(SuburbDto suburbDto) {
        return Suburb.builder()
                .suburbName(suburbDto.getSuburbName())
                .postCode(suburbDto.getPostCode())
                .state(suburbDto.getState())
                .country(suburbDto.getCountry())
                .build();
    }
}
