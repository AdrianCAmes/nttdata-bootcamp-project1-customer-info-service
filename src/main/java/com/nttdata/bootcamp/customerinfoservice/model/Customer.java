package com.nttdata.bootcamp.customerinfoservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "customer")
public class Customer {
    @Id
    private String id;
    private String type;
    private String status;
    private PersonDetails personDetails;
    private BusinessDetails businessDetails;
}