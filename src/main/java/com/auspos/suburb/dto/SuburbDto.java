package com.auspos.suburb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuburbDto {

    private Long id;

    @NotBlank
    private String suburbName;

    @NotNull
    private Long postCode;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

}
