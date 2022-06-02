package com.github.dericksm.paymentservice.mapper;

import com.github.dericksm.paymentservice.model.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address addressToAddress(com.github.dericksm.common.model.Address address);
}
