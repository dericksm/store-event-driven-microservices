package com.github.dericksm.common.model;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Address {

    @NotEmpty
    private String line1;

    @NotEmpty
    private String city;

    private String state;

    @NotEmpty
    private String zipCode;


}