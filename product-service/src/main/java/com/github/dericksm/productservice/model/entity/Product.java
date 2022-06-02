package com.github.dericksm.productservice.model.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    @Indexed(unique=true)
    private String name;
    @NotNull
    private Double price;
    @PositiveOrZero
    private Integer quantity;
    @PositiveOrZero
    private Integer reservedQuantity = 0;

    public void addItem() {
        quantity += 1;
    }

    public void removeItem() {
        quantity -= 1;
    }

    public boolean isStockAvailable(int requiredQuantity) {
        return this.quantity >= requiredQuantity;
    }

    public Product reserveStock(int quantityToBeReserved) {
        this.reservedQuantity += quantityToBeReserved;
        this.quantity -= quantityToBeReserved;
        return this;
    }

    public Product releaseStock(int quantityToBeReleased) {
        this.reservedQuantity -= quantityToBeReleased;
        this.quantity += quantityToBeReleased;
        return this;
    }
}
