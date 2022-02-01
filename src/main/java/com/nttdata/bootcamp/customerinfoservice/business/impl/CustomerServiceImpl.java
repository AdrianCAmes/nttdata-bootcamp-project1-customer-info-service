package com.nttdata.bootcamp.customerinfoservice.business.impl;

import com.nttdata.bootcamp.customerinfoservice.business.CustomerService;
import com.nttdata.bootcamp.customerinfoservice.config.Constants;
import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerCreateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerUpdateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.repository.CustomerRepository;
import com.nttdata.bootcamp.customerinfoservice.utils.CustomerUtils;
import com.nttdata.bootcamp.customerinfoservice.utils.errorhandling.DuplicatedUniqueFieldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
    private final Constants constants;

    @Override
    public Mono<Customer> create(CustomerCreateRequestDTO customerDTO) {
        log.info("Start of operation to create a customer");

        log.info("Validating customer uniqueness");
        Customer transformedCustomer = customerUtils.customerCreateRequestDTOToCustomer(customerDTO);
        Mono<Customer> createdCustomer = findAll()
                .filter(retrievedCustomer -> customerUtils.uniqueValuesDuplicity(transformedCustomer, retrievedCustomer))
                .hasElements().flatMap(isARepeatedCustomer -> {
                    if (Boolean.TRUE.equals(isARepeatedCustomer)) {
                        log.warn("Customer does not accomplish with uniqueness specifications");
                        log.warn("Proceeding to abort create operation");
                        return Mono.error(new DuplicatedUniqueFieldException("Customer does not accomplish with uniqueness specifications"));
                    }
                    else {
                        transformedCustomer.setStatus(constants.getStatusActive());
                        log.info("Creating new customer: [{}]", transformedCustomer);
                        Mono<Customer> newCustomer = customerRepository.insert(transformedCustomer);
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
    public Mono<Customer> update(CustomerUpdateRequestDTO customerDTO) {
        log.info("Start of operation to update a customer");

        log.info("Validating customer existence");
        Customer transformedCustomer = customerUtils.customerUpdateRequestDTOToCustomer(customerDTO);
        Mono<Customer> updatedCustomer = findById(transformedCustomer.getId())
                .hasElement().flux().flatMap(customerExists -> {
                    if (Boolean.TRUE.equals(customerExists)) {
                        log.info("Customer with id [{}] exists in database", transformedCustomer.getId());
                        log.info("Proceeding to validate customer uniqueness");
                        return findAll();
                    } else {
                        log.warn("Customer with id [{}] does not exist in database", transformedCustomer.getId());
                        log.warn("Proceeding to abort update operation");
                        return Mono.error(new NoSuchElementException("Customer does not exist in database"));
                    }
                })
                .filter(retrievedCustomer -> customerUtils.uniqueValuesDuplicity(transformedCustomer, retrievedCustomer))
                .hasElements().flatMap(isARepeatedCustomer -> {
                    if (Boolean.TRUE.equals(isARepeatedCustomer)) {
                        log.warn("Customer does not accomplish with uniqueness specifications");
                        log.warn("Proceeding to abort update operation");
                        return Mono.error(new DuplicatedUniqueFieldException("Customer does not accomplish with uniqueness specifications"));
                    }
                    else {
                        log.info("Updating customer: [{}]", transformedCustomer);
                        Mono<Customer> newCustomer = customerRepository.save(transformedCustomer);
                        log.info("Customer with id: [{}] was successfully updated", transformedCustomer.getId());

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

    @Override
    @KafkaListener(topics = "${constants.kafka.credits-topic}", groupId = "group_id")
    public void kafkaListener(String credit) {
        log.info("New credit was created for customer: " + credit);
    }
}
