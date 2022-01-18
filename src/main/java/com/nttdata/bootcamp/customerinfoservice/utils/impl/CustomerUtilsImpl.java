package com.nttdata.bootcamp.customerinfoservice.utils.impl;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import com.nttdata.bootcamp.customerinfoservice.config.Constants;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerCreateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.model.dto.request.CustomerUpdateRequestDTO;
import com.nttdata.bootcamp.customerinfoservice.utils.CustomerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerUtilsImpl implements CustomerUtils {

    private final Constants constants;

    public Boolean uniqueValuesDuplicity(Customer customerA, Customer customerB) {

        // A customer can not be duplicated by its own
        if (customerA.getId() != null &&
                customerB.getId() != null &&
                customerA.getId().contentEquals(customerB.getId())) return false;

        // If it's evaluating different customer groups (personal and business) then unique values do not repeat each other
        if (!customerA.getCustomerType().getGroup().contentEquals(customerB.getCustomerType().getGroup())) return false;

        if (customerA.getCustomerType().getGroup().contentEquals(constants.getCustomerPersonalGroup())) {
            // Unique value for Personal Accounts is Identity Number
            return customerA.getPersonDetails().getIdentityNumber().contentEquals(customerB.getPersonDetails().getIdentityNumber());

        } else if (customerA.getCustomerType().getGroup().contentEquals(constants.getCustomerBusinessGroup())) {
            // Unique values for Business Accounts are RUC and Name
            return customerA.getBusinessDetails().getRuc().contentEquals(customerB.getBusinessDetails().getRuc()) ||
                    customerA.getBusinessDetails().getName().contentEquals(customerB.getBusinessDetails().getName());

        } else return false;
    }

    @Override
    public Customer customerCreateRequestDTOToCustomer(CustomerCreateRequestDTO customerDTO) {
        return Customer.builder()
                .customerType(customerDTO.getCustomerType())
                .personDetails(customerDTO.getPersonDetails())
                .businessDetails(customerDTO.getBusinessDetails())
                .build();
    }

    @Override
    public Customer customerUpdateRequestDTOToCustomer(CustomerUpdateRequestDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.getId())
                .status(customerDTO.getStatus())
                .customerType(customerDTO.getCustomerType())
                .personDetails(customerDTO.getPersonDetails())
                .businessDetails(customerDTO.getBusinessDetails())
                .build();
    }

    @Override
    public CustomerCreateRequestDTO customerToCustomerCreateRequestDTO(Customer customer) {
        return CustomerCreateRequestDTO.builder()
                .customerType(customer.getCustomerType())
                .personDetails(customer.getPersonDetails())
                .businessDetails(customer.getBusinessDetails())
                .build();
    }

    @Override
    public CustomerUpdateRequestDTO customerToCustomerUpdateRequestDTO(Customer customer) {
        return CustomerUpdateRequestDTO.builder()
                .id(customer.getId())
                .status(customer.getStatus())
                .customerType(customer.getCustomerType())
                .personDetails(customer.getPersonDetails())
                .businessDetails(customer.getBusinessDetails())
                .build();
    }
}
