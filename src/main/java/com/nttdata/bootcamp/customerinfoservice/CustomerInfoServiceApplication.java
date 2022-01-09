package com.nttdata.bootcamp.customerinfoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableDiscoveryClient
public class CustomerInfoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerInfoServiceApplication.class, args);
	}

}
