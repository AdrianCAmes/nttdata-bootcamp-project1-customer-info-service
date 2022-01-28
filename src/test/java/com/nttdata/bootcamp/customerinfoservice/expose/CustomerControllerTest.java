package com.nttdata.bootcamp.customerinfoservice.expose;


import com.nttdata.bootcamp.customerinfoservice.business.CustomerService;
import com.nttdata.bootcamp.customerinfoservice.config.Constants;
import com.nttdata.bootcamp.customerinfoservice.model.*;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerCreateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerUpdateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.utils.CustomerUtils;
import com.nttdata.bootcamp.customerinfoservice.utils.errorhandling.DuplicatedUniqueFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private CustomerUtils customerUtils;
    @Autowired
    private Constants constants;
    @MockBean
    private CustomerService customerService;

    private static Customer customerMock1 = new Customer();
    private static Customer customerMock2 = new Customer();

    @BeforeEach
    void setUpEach() {
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
                        .group(constants.getCustomerPersonalGroup())
                        .subgroup("Mock")
                        .build())
                .status(constants.getStatusActive())
                .personDetails(representative)
                .build();

        ArrayList<PersonDetails> representatives = new ArrayList<>();
        representatives.add(representative);
        customerMock2 = Customer.builder()
                .id("2")
                .customerType(CustomerType
                        .builder()
                        .group(constants.getCustomerBusinessGroup())
                        .subgroup("Mock")
                        .build())
                .status(constants.getStatusActive())
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
    }

    @Test
    void findAllCustomers() {
        when(customerService.findAll()).thenReturn(Flux.just(customerMock1, customerMock2));

        Flux<Customer> retrievedCustomers = webTestClient
                .get()
                .uri("/api/v1/customers")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Customer.class)
                .getResponseBody();

        StepVerifier.create(retrievedCustomers)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock1.toString()))
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock2.toString()))
                .verifyComplete();
    }

    @Test
    void findCustomerById() {
        when(customerService.findById(customerMock1.getId())).thenReturn(Mono.just(customerMock1));

        Flux<Customer> retrievedCustomer = webTestClient
                .get()
                .uri("/api/v1/customers/" + customerMock1.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(Customer.class)
                .getResponseBody();

        StepVerifier.create(retrievedCustomer)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock1.toString()))
                .verifyComplete();
    }

    @Test
    void createCustomer() {
        when(customerService.create(any(CustomerCreateRequestDTO.class))).thenReturn(Mono.just(customerMock1));

        CustomerCreateRequestDTO customerToSave = customerUtils.customerToCustomerCreateRequestDTO(customerMock1);
        Flux<Customer> savedCustomer = webTestClient
                .post()
                .uri("/api/v1/customers")
                .body(Mono.just(customerToSave), CustomerCreateRequestDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Customer.class)
                .getResponseBody();

        StepVerifier.create(savedCustomer)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock1.toString()))
                .verifyComplete();
    }

    @Test
    void createWithCustomerDuplicityError() {
        when(customerService.create(any(CustomerCreateRequestDTO.class))).thenReturn(Mono.error(new DuplicatedUniqueFieldException("Customer does not accomplish with uniqueness specifications")));

        CustomerCreateRequestDTO customerToSave = customerUtils.customerToCustomerCreateRequestDTO(customerMock1);
        webTestClient
                .post()
                .uri("/api/v1/customers")
                .body(Mono.just(customerToSave), CustomerCreateRequestDTO.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateCustomer() {
        when(customerService.update(any(CustomerUpdateRequestDTO.class))).thenReturn(Mono.just(customerMock1));

        CustomerUpdateRequestDTO customerToUpdate = customerUtils.customerToCustomerUpdateRequestDTO(customerMock1);
        Flux<Customer> updatedCustomer = webTestClient
                .put()
                .uri("/api/v1/customers")
                .body(Mono.just(customerToUpdate), CustomerUpdateRequestDTO.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Customer.class)
                .getResponseBody();

        StepVerifier.create(updatedCustomer)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock1.toString()))
                .verifyComplete();
    }

    @Test
    void updateWithCustomerNotFoundError() {
        when(customerService.update(any(CustomerUpdateRequestDTO.class))).thenReturn(Mono.error(new NoSuchElementException("Customer does not exist in database")));

        CustomerUpdateRequestDTO customerToUpdate = customerUtils.customerToCustomerUpdateRequestDTO(customerMock1);
        webTestClient
                .put()
                .uri("/api/v1/customers")
                .body(Mono.just(customerToUpdate), CustomerUpdateRequestDTO.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateWithCustomerDuplicityError() {
        when(customerService.update(any(CustomerUpdateRequestDTO.class))).thenReturn(Mono.error(new DuplicatedUniqueFieldException("Customer does not accomplish with uniqueness specifications")));

        CustomerUpdateRequestDTO customerToUpdate = customerUtils.customerToCustomerUpdateRequestDTO(customerMock1);
        webTestClient
                .put()
                .uri("/api/v1/customers")
                .body(Mono.just(customerToUpdate), CustomerUpdateRequestDTO.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void deleteCustomer() {
        when(customerService.removeById(customerMock1.getId())).thenReturn(Mono.just(customerMock1));

        Flux<Customer> removedCustomer = webTestClient
                .delete()
                .uri("/api/v1/customers/" + customerMock1.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(Customer.class)
                .getResponseBody();

        StepVerifier.create(removedCustomer)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock1.toString()))
                .verifyComplete();
    }
}