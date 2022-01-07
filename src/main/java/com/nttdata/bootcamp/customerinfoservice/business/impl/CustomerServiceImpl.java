package com.nttdata.bootcamp.customerinfoservice.business.impl;

import com.nttdata.bootcamp.customerinfoservice.business.CustomerService;
import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import com.nttdata.bootcamp.customerinfoservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final WebClient webclient;

    @Override
    public Mono<Customer> create(Mono<Customer> personMono) {
        return null;
    }

    @Override
    public Mono<Customer> findById(String id) {
        return null;
    }

    @Override
    public Flux<Customer> findAll() {
        return null;
    }

    @Override
    public Mono<Customer> update(Mono<Customer> personMono) {
        return null;
    }

    @Override
    public Mono<Customer> change(Mono<Customer> personMono) {
        return null;
    }

    @Override
    public Mono<Customer> removeById(String id) {
        return null;
    }
}
