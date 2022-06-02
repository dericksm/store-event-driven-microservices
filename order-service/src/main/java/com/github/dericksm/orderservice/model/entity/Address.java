package com.github.dericksm.orderservice.model.entity;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
@RequiredArgsConstructor
public class Address {

    @NotEmpty
    private String line1;

    @NotEmpty
    private String city;

    private String state;

    @NotEmpty
    private String zipCode;


}