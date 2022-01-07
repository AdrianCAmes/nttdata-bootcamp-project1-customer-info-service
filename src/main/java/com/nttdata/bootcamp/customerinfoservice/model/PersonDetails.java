package com.nttdata.bootcamp.customerinfoservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetails {
    private String name;
    private String lastname;
    private String identityNumber;
    private Address address;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private Date birthdate;
}