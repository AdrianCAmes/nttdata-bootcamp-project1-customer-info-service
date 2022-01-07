package com.nttdata.bootcamp.customerinfoservice.expose;

import com.nttdata.bootcamp.customerinfoservice.business.CustomerService;
import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    public Flux<Customer> findAllCustomers(){
        log.info("Get operation in /customers");
        return customerService.findAll();
    }

    @GetMapping("/customers/{id}")
    public Mono<ResponseEntity<Customer>> findCustomerById(@PathVariable("id") String id) {
        log.info("Get operation in /customers/{}", id);
        return customerService.findById(id)
                .flatMap(retrievedCustomer -> Mono.just(ResponseEntity.ok(retrievedCustomer)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
                //.onErrorResume(IllegalArgumentException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage())));
                //.onErrorReturn(error -> Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/customers")
    public Mono<ResponseEntity<Object>> createCustomer(@RequestBody Customer customer) {
        log.info("Post operation in /customers");
        return customerService.create(customer)
                .flatMap(createdCustomer -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer)));
    }

    @PutMapping("/customers")
    public Mono<ResponseEntity<Customer>> updateCustomer(@RequestBody Customer customer) {
        log.info("Put operation in /customers");
        return customerService.update(customer)
                .flatMap(updatedCustomer -> Mono.just(ResponseEntity.ok(updatedCustomer)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/customers/{id}")
    public Mono<ResponseEntity<Customer>> deleteCustomer(@PathVariable("id") String id) {
        log.info("Delete operation in /customers/{}", id);
        return customerService.removeById(id)
                .flatMap(removedCustomer -> Mono.just(ResponseEntity.ok(removedCustomer)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
