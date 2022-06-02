package com.github.dericksm.paymentservice.model.entity;

import java.math.BigDecimal;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class InvoiceItem {

    @NotEmpty
    private String productId;
    @NotEmpty
    private String productName;
    @Positive
    private Integer quantity;
    @Positive
    private BigDecimal productPrice;
    @Positive
    private BigDecimal total;

    public InvoiceItem() {
    }

    public InvoiceItem(String productId, String productName, Integer quantity, BigDecimal productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.productPrice = productPrice;
        calculateTotal();
    }

    public void calculateTotal() {
        this.total = this.productPrice.multiply(new BigDecimal(this.quantity));
    }
}
