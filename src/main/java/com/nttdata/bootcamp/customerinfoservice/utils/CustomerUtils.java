package com.nttdata.bootcamp.customerinfoservice.utils;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerCreateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerUpdateRequestDTO;

public interface CustomerUtils {
    Boolean uniqueValuesDuplicity(Customer customerA, Customer customerB);
    Customer customerCreateRequestDTOToCustomer(CustomerCreateRequestDTO customerDTO);
    Customer customerUpdateRequestDTOToCustomer(CustomerUpdateRequestDTO customerDTO);
    CustomerCreateRequestDTO customerToCustomerCreateRequestDTO(Customer customer);
    CustomerUpdateRequestDTO customerToCustomerUpdateRequestDTO(Customer customer);
}
