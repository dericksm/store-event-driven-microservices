package com.github.dericksm.common.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {

    private String id;
    private TransactionStatus transactionStatus;
    private List<TransactionListener> listeners = new ArrayList();


    public void addListener(TransactionListener listener) {
        listeners.add(listener);
    }

    public void updateListener(String serviceId, TransactionStatus status) {
        var listener = listeners.stream().filter(lt -> lt.getServiceId().equals(serviceId)).findFirst().get();
        listener.setTransactionStatus(status);
    }
}
