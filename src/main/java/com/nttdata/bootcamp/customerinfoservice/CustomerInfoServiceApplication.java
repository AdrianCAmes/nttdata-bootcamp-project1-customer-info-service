package com.nttdata.bootcamp.customerinfoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class CustomerInfoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerInfoServiceApplication.class, args);
	}

}
