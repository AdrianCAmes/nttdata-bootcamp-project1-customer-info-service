package com.nttdata.bootcamp.customerinfoservice.business;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<Customer> create(Customer customer);

    Mono<Customer> findById(String id);

    Flux<Customer> findAll();

    Mono<Customer> update(Customer customer);

    Mono<Customer> removeById(String id);
}
