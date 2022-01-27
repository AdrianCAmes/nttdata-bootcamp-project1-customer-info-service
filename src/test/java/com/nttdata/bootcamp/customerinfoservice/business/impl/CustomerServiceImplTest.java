package com.nttdata.bootcamp.customerinfoservice.business.impl;

import com.nttdata.bootcamp.customerinfoservice.business.CustomerService;
import com.nttdata.bootcamp.customerinfoservice.model.*;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerCreateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerUpdateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.repository.CustomerRepository;
import com.nttdata.bootcamp.customerinfoservice.utils.CustomerUtils;
import com.nttdata.bootcamp.customerinfoservice.utils.errorhandling.DuplicatedUniqueFieldException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest()
class CustomerServiceImplTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerUtils customerUtils;
    @MockBean
    private CustomerRepository customerRepository;

    private static Customer customerMock1 = new Customer();
    private static Customer customerMock2 = new Customer();
    private static Customer customerMock3 = new Customer();

    @BeforeAll
    static void setUp() {
        PersonDetails representative =  PersonDetails.builder()
                .name("Marco")
                .lastname("Cruz")
                .identityNumber("74854687")
                .address(Address.builder().
                        number(144)
                        .street("Av. Proceres")
                        .city("Lima")
                        .country("Peru")
                        .build())
                .email("marco.cruz@gmail.com")
                .phoneNumber("5412458")
                .mobileNumber("947854120")
                .birthdate(new Date(2000, 10, 25))
                .build();

        customerMock1 = Customer.builder()
                .id("1")
                .customerType(CustomerType
                        .builder()
                        .group("Personal")
                        .subgroup("Standard")
                        .build())
                .status("Active")
                .personDetails(representative)
                .build();

        ArrayList<PersonDetails> representatives = new ArrayList<>();
        representatives.add(representative);
        customerMock2 = Customer.builder()
                .id("2")
                .customerType(CustomerType
                        .builder()
                        .group("Business")
                        .subgroup("Standard")
                        .build())
                .status("Active")
                .businessDetails(BusinessDetails.builder()
                        .name("NTT Data Peru")
                        .ruc("20874563347")
                        .address(Address.builder().
                                number(258)
                                .street("Av. Javier Prado")
                                .city("Lima")
                                .country("Peru")
                                .build())
                        .representatives(representatives)
                        .build())
                .build();

        customerMock3 = Customer.builder()
                .id("3")
                .customerType(CustomerType
                        .builder()
                        .group("Personal")
                        .subgroup("Standard")
                        .build())
                .status("Active")
                .personDetails(representative)
                .build();
    }

    @Test
    void create() {
        when(customerRepository.findAll()).thenReturn(Flux.just(customerMock2));
        when(customerRepository.insert(any(Customer.class))).thenReturn(Mono.just(customerMock1));

        CustomerCreateRequestDTO customerToSave = customerUtils.customerToCustomerCreateRequestDTO(customerMock1);
        Mono<Customer> savedCustomer = customerService.create(customerToSave);

        StepVerifier.create(savedCustomer)
                .expectSubscription()
                .expectNext(customerMock1)
                .verifyComplete();
    }

    @Test
    void createWithCustomerDuplicityError() {
        when(customerRepository.findAll()).thenReturn(Flux.just(customerMock3));

        CustomerCreateRequestDTO customerToSave = customerUtils.customerToCustomerCreateRequestDTO(customerMock1);
        Mono<Customer> savedCustomer = customerService.create(customerToSave);

        StepVerifier.create(savedCustomer)
                .expectSubscription()
                .expectError(DuplicatedUniqueFieldException.class)
                .verify();
    }

    @Test
    void findById() {
        when(customerRepository.findById(customerMock1.getId())).thenReturn(Mono.just(customerMock1));

        Mono<Customer> retrievedCustomer = customerService.findById(customerMock1.getId());

        StepVerifier.create(retrievedCustomer)
                .expectSubscription()
                .expectNext(customerMock1)
                .verifyComplete();
    }

    @Test
    void findAll() {
        when(customerRepository.findAll()).thenReturn(Flux.just(customerMock1, customerMock2));

        Flux<Customer> retrievedCustomers = customerService.findAll() ;

        StepVerifier.create(retrievedCustomers)
                .expectSubscription()
                .expectNext(customerMock1)
                .expectNext(customerMock2)
                .verifyComplete();
    }

    @Test
    void update() {
        when(customerRepository.findById(customerMock1.getId())).thenReturn(Mono.just(customerMock1));
        when(customerRepository.findAll()).thenReturn(Flux.just(customerMock1, customerMock2));
        when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(customerMock1));

        CustomerUpdateRequestDTO customerToUpdate = customerUtils.customerToCustomerUpdateRequestDTO(customerMock1);
        Mono<Customer> updatedCustomer = customerService.update(customerToUpdate);

        StepVerifier.create(updatedCustomer)
                .expectSubscription()
                .expectNext(customerMock1)
                .verifyComplete();
    }

    @Test
    void updateWithCustomerNotFoundError() {
        when(customerRepository.findById(customerMock1.getId())).thenReturn(Mono.empty());

        CustomerUpdateRequestDTO customerToUpdate = customerUtils.customerToCustomerUpdateRequestDTO(customerMock1);
        Mono<Customer> updatedCustomer = customerService.update(customerToUpdate);

        StepVerifier.create(updatedCustomer)
                .expectSubscription()
                .expectError(NoSuchElementException.class)
                .verify();
    }


    @Test
    void updateWithCustomerDuplicityError() {
        when(customerRepository.findById(customerMock1.getId())).thenReturn(Mono.just(customerMock1));
        when(customerRepository.findAll()).thenReturn(Flux.just(customerMock1, customerMock2, customerMock3));

        CustomerUpdateRequestDTO customerToUpdate = customerUtils.customerToCustomerUpdateRequestDTO(customerMock1);
        Mono<Customer> updatedCustomer = customerService.update(customerToUpdate);

        StepVerifier.create(updatedCustomer)
                .expectSubscription()
                .expectError(DuplicatedUniqueFieldException.class)
                .verify();
    }

    @Test
    void removeById() {
        when(customerRepository.findById(customerMock1.getId())).thenReturn(Mono.just(customerMock1));
        when(customerRepository.deleteById(customerMock1.getId())).thenReturn(Mono.empty());

        Mono<Customer> removedCustomer = customerService.removeById(customerMock1.getId());

        StepVerifier.create(removedCustomer)
                .expectSubscription()
                .expectNext(customerMock1)
                .verifyComplete();
    }
}