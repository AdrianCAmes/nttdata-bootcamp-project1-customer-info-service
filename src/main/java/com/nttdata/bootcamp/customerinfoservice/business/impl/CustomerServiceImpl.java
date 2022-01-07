package com.nttdata.bootcamp.customerinfoservice.business.impl;

import com.nttdata.bootcamp.customerinfoservice.business.CustomerService;
import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import com.nttdata.bootcamp.customerinfoservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Mono<Customer> create(Customer customer) {
        log.info("Start of operation to create a customer");

        log.info("Creating new customer: [{}]", customer.toString());
        Mono<Customer> createdUser = customerRepository.insert(customer);
        log.info("New customer was created successfully");

        log.info("End of operation to create a customer");
        return createdUser;
    }

    @Override
    public Mono<Customer> findById(String id) {
        log.info("Start of operation to find a customer by id");

        log.info("Retrieving customer with id: [{}]", id);
        Mono<Customer> retrievedCustomer = customerRepository.findById(id);
        log.info("Customer with id: [{}] was retrieved successfully", id);

        log.info("End of operation to find a customer by id");
        return retrievedCustomer;
    }

    @Override
    public Flux<Customer> findAll() {
        log.info("Start of operation to retrieve all customers");

        log.info("Retrieving all customers");
        Flux<Customer> retrievedCustomers = customerRepository.findAll();
        log.info("All customers retrieved successfully");

        log.info("End of operation to retrieve all customers");
        return retrievedCustomers;
    }

    @Override
    public Mono<Customer> update(Customer customer) {
        log.info("Start of operation to update a customer");

        log.info("Updating customer with id: [{}]", customer.getId());
        Mono<Customer> updatedCustomer = findById(customer.getId())
                .flatMap(customerDB -> customerRepository.save(customer));
        log.info("Customer with id: [{}] was successfully updated", customer.getId());


        log.info("End of operation to update a customer");
        return updatedCustomer;
    }

    @Override
    public Mono<Customer> removeById(String id) {
        log.info("Start of operation to remove a customer");

        log.info("Deleting customer with id: [{}]", id);
        Mono<Customer> updatedCustomer = findById(id)
                .flatMap(customerDB -> customerRepository.deleteById(customerDB.getId()).thenReturn(customerDB));
        log.info("Customer with id: [{}] was successfully deleted", id);

        log.info("End of operation to remove a customer");
        return updatedCustomer;
    }
}
