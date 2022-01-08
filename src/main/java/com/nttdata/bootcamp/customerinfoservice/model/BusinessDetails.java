package com.nttdata.bootcamp.customerinfoservice.model;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusinessDetails {
    private String name;
    private String ruc;
    private Address address;
    private ArrayList<PersonDetails> representatives;
}