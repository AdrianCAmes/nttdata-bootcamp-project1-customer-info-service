package com.nttdata.bootcamp.customerinfoservice.repository;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}