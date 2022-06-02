package com.github.dericksm.paymentservice.repository;

import com.github.dericksm.paymentservice.model.entity.Invoice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends ReactiveMongoRepository<Invoice, String> {

}
