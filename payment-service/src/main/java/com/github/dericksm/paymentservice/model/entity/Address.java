package com.github.dericksm.paymentservice.model.entity;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

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