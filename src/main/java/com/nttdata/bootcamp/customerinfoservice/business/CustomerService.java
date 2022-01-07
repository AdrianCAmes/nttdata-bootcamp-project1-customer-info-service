package com.nttdata.bootcamp.customerinfoservice.business;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<Customer> create(Mono<Customer> personMono);

    Mono<Customer> findById(String id);

    Flux<Customer> findAll();

    Mono<Customer> update(Mono<Customer> personMono);

    Mono<Customer> change(Mono<Customer> personMono);

    Mono<Customer> removeById(String id);
}
