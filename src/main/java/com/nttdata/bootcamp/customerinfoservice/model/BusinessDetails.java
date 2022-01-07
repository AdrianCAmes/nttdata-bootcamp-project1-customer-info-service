package com.nttdata.bootcamp.customerinfoservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetails {
    private String name;
    private String ruc;
    private Address address;
    private ArrayList<PersonDetails> representatives;
}