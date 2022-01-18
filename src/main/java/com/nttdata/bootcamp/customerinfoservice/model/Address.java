package com.nttdata.bootcamp.customerinfoservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {
    private Integer number;
    private String street;
    private String city;
    private String country;
}