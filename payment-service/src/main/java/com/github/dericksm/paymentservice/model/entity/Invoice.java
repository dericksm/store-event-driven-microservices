package com.github.dericksm.paymentservice.model.entity;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    private String id;
    @NotEmpty
    private String orderId;
    @NotNull
    private PaymentDetails paymentDetails;
    @Positive
    private BigDecimal totalCharge;
    @NotNull
    private Address billingAddress;
    @NotEmpty
    private List<InvoiceItem> invoiceItems;

    public void calculateTotal() {
        this.totalCharge = invoiceItems.stream()
            .map(InvoiceItem::getProductPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
