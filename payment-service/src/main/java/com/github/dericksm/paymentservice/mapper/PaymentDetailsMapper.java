package com.github.dericksm.paymentservice.mapper;

import com.github.dericksm.paymentservice.model.entity.PaymentDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentDetailsMapper {

    PaymentDetails paymentDetailsToPaymentDetails(com.github.dericksm.common.model.PaymentDetails paymentDetails);
}
