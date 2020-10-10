package com.auspos.suburb.service;

import com.auspos.suburb.domain.Suburb;
import com.auspos.suburb.dto.SuburbDto;
import com.auspos.suburb.exception.DuplicateSuburbException;
import com.auspos.suburb.repository.SuburbRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.stream.Stream;

import static com.auspos.suburb.domain.Suburb.builder;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SuburbServiceUnitTest {

    @Mock
    private SuburbRepository suburbRepository;

    private SuburbDtoMapper suburbDtoMapper = new SuburbDtoMapper();

    private SuburbService suburbService;

    @BeforeEach
    public void setup() {
        suburbService = new SuburbService(suburbRepository, suburbDtoMapper);
    }

    @ParameterizedTest
    @MethodSource("validSuburbRepoResponse")
    public void getSuburbsByPostCode_validPostCode_shouldReturnSuburb(List<Suburb> dataFromRepo) {
        when(suburbRepository.findByPostCode(anyLong())).thenReturn(dataFromRepo);

        List<SuburbDto> suburbDtos = suburbService.getSuburbsByPostCode(3027);

        verify(suburbRepository, times(1)).findByPostCode(3027);
        assertThat(suburbDtos.size(), equalTo(dataFromRepo.size()));

        SuburbDto suburbDto = suburbDtos.get(0);
        assertThat(suburbDto.getId(), equalTo(10L));
        assertThat(suburbDto.getSuburbName(), equalTo("Williams Landing"));
        assertThat(suburbDto.getPostCode(), equalTo(3027L));
        assertThat(suburbDto.getState(), equalTo("Victoria"));
        assertThat(suburbDto.getCountry(), equalTo("Australia"));
    }

    @Test
    public void getSuburbsByPostCode_noSuburbFound_shouldReturnEmptyList() {
        when(suburbRepository.findByPostCode(anyLong())).thenReturn(null);
        List<SuburbDto> suburbDtos = suburbService.getSuburbsByPostCode(3027);

        verify(suburbRepository, times(1)).findByPostCode(3027);
        assertThat(suburbDtos.size(), equalTo(0));
    }

    @ParameterizedTest
    @MethodSource("validSuburbRepoResponse")
    public void getSuburbsBySuburbName_validPostCode_shouldReturnSuburb(List<Suburb> dataFromRepo) {
        when(suburbRepository.findBySuburbNameIgnoreCase(anyString())).thenReturn(dataFromRepo);
        List<SuburbDto> suburbDtos = suburbService.getSuburbsBySuburbName("Williams Landing");

        verify(suburbRepository, times(1)).findBySuburbNameIgnoreCase("Williams Landing");
        assertThat(suburbDtos.size(), equalTo(dataFromRepo.size()));

        SuburbDto suburbDto = suburbDtos.get(0);
        assertThat(suburbDto.getId(), equalTo(10L));
        assertThat(suburbDto.getSuburbName(), equalTo("Williams Landing"));
        assertThat(suburbDto.getPostCode(), equalTo(3027L));
        assertThat(suburbDto.getState(), equalTo("Victoria"));
        assertThat(suburbDto.getCountry(), equalTo("Australia"));
    }

    @Test
    public void getSuburbsBySuburbName_noSuburbFound_shouldReturnEmptyList() {
        when(suburbRepository.findBySuburbNameIgnoreCase(anyString())).thenReturn(null);
        List<SuburbDto> suburbDtos = suburbService.getSuburbsBySuburbName("Williams Landing");

        verify(suburbRepository, times(1)).findBySuburbNameIgnoreCase("Williams Landing");
        assertThat(suburbDtos.size(), equalTo(0));
    }

    private static Stream<Arguments> validSuburbRepoResponse() {
        return Stream.of (
                // Repo returns single suburb
                Arguments.of(asList(builder().id(10).suburbName("Williams Landing").postCode(3027).state("Victoria").country("Australia").build())),

                // Repo returns multiple suburbs
                Arguments.of(asList(builder().id(10).suburbName("Williams Landing").postCode(3027).state("Victoria").country("Australia").build(),
                        builder().id(2).suburbName("Hoppers Crossing").postCode(3029).state("Victoria").country("Australia").build(),
                        builder().id(3).suburbName("Point Cook").postCode(3030).state("Victoria").country("Australia").build(),
                        builder().id(4).suburbName("Werribee").postCode(3030).state("Victoria").country("Australia").build()))
        );

    }

    @Test
    public void createSuburb_validData_shouldAddSuburb() {
        when(suburbRepository.findBySuburbNameAndPostCode(anyString(), anyLong())).thenReturn(null);

        suburbService.createSuburb(SuburbDto.builder().suburbName("Williams Landing").postCode(3027L).state("Victoria").country("Australia").build());

        verify(suburbRepository, times(1)).findBySuburbNameAndPostCode("Williams Landing", 3027);
        verify(suburbRepository, times(1)).save(any(Suburb.class));

    }

    @Test
    public void createSuburb_suburbAlreadyExists_shouldThrowException() {
        when(suburbRepository.findBySuburbNameAndPostCode(anyString(), anyLong()))
                .thenReturn(asList(builder().id(10).suburbName("Williams Landing").postCode(3027).state("Victoria").country("Australia").build()));

        SuburbDto suburbDto = SuburbDto.builder().suburbName("Williams Landing").postCode(3027L).state("Victoria").country("Australia").build();
        Assertions.assertThrows(DuplicateSuburbException.class, () -> suburbService.createSuburb(suburbDto));

        verify(suburbRepository, times(1)).findBySuburbNameAndPostCode("Williams Landing", 3027);
        verify(suburbRepository, never()).save(any(Suburb.class));
    }

}
