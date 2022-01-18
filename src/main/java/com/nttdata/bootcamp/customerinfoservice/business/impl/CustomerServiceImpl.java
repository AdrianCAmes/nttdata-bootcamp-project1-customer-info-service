package com.nttdata.bootcamp.customerinfoservice.business.impl;

import com.nttdata.bootcamp.customerinfoservice.business.CustomerService;
import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import com.nttdata.bootcamp.customerinfoservice.repository.CustomerRepository;
import com.nttdata.bootcamp.customerinfoservice.utils.CustomerUtils;
import com.nttdata.bootcamp.customerinfoservice.utils.errorhandling.DuplicatedUniqueFieldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerUtils customerUtils;

    @Override
    public Mono<Customer> create(Customer customer) {
        log.info("Start of operation to create a customer");

        log.info("Validating customer uniqueness");
        Mono<Customer> createdCustomer = findAll()
                .filter(retrievedCustomer -> customerUtils.uniqueValuesDuplicity(customer, retrievedCustomer))
                .hasElements().flatMap(isARepeatedCustomer -> {
                    if (Boolean.TRUE.equals(isARepeatedCustomer)) {
                        log.warn("Customer does not accomplish with uniqueness specifications");
                        log.warn("Proceeding to abort create operation");
                        return Mono.error(new DuplicatedUniqueFieldException("Customer does not accomplish with uniqueness specifications"));
                    }
                    else {
                        log.info("Creating new customer: [{}]", customer.toString());
                        Mono<Customer> newCustomer = customerRepository.insert(customer);
                        log.info("New customer was created successfully");

                        return newCustomer;
                    }
                });

        log.info("End of operation to create a customer");
        return createdCustomer;
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

        log.info("Validating customer existence");
        Mono<Customer> updatedCustomer = findById(customer.getId())
                .hasElement().flux().flatMap(customerExists -> {
                    if (Boolean.TRUE.equals(customerExists)) {
                        log.info("Customer with id [{}] exists in database", customer.getId());
                        log.info("Proceeding to validate customer uniqueness");
                        return findAll();
                    } else {
                        log.warn("Customer with id [{}] does not exist in database", customer.getId());
                        log.warn("Proceeding to abort update operation");
                        return Flux.error(new NoSuchElementException("Customer does not exist in database"));
                    }
                })
                .filter(retrievedCustomer -> customerUtils.uniqueValuesDuplicity(customer, retrievedCustomer))
                .hasElements().flatMap(isARepeatedCustomer -> {
                    if (Boolean.TRUE.equals(isARepeatedCustomer)) {
                        log.warn("Customer does not accomplish with uniqueness specifications");
                        log.warn("Proceeding to abort update operation");
                        return Mono.error(new DuplicatedUniqueFieldException("Customer does not accomplish with uniqueness specifications"));
                    }
                    else {
                        log.info("Updating customer: [{}]", customer.toString());
                        Mono<Customer> newCustomer = customerRepository.save(customer);
                        log.info("Customer with id: [{}] was successfully updated", customer.getId());

                        return newCustomer;
                    }
                });

        log.info("End of operation to update a customer");
        return updatedCustomer;
    }

    @Override
    public Mono<Customer> removeById(String id) {
        log.info("Start of operation to remove a customer");

        log.info("Deleting customer with id: [{}]", id);
        Mono<Customer> removedCustomer = findById(id)
                .flatMap(retrievedCustomer -> customerRepository.deleteById(retrievedCustomer.getId()).thenReturn(retrievedCustomer));
        log.info("Customer with id: [{}] was successfully deleted", id);

        log.info("End of operation to remove a customer");
        return removedCustomer;
    }
}
