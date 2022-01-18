package com.nttdata.bootcamp.customerinfoservice.business;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerCreateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerUpdateRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<Customer> create(CustomerCreateRequestDTO customerDTO);

    Mono<Customer> findById(String id);

    Flux<Customer> findAll();

    Mono<Customer> update(CustomerUpdateRequestDTO customerDTO);

    Mono<Customer> removeById(String id);
}
