package com.auspos.suburb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuburbDto {

    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$")
    private String suburbName;

    @NotNull
    @Max(999999)
    private Long postCode;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$")
    private String state;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$")
    private String country;

}
