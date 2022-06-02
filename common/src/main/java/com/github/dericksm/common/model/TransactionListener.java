package com.github.dericksm.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionListener {

    private String serviceId;
    private TransactionStatus transactionStatus;
}
