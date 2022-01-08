package com.nttdata.bootcamp.customerinfoservice.utils;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerUtils {

    private final Constants constants;

    public Boolean uniqueValuesDuplicity(Customer customerA, Customer customerB) {

        log.info("unique: " + customerA.toString());
        // A customer can not be duplicated by its own
        if (customerA.getId() != null &&
                customerB.getId() != null &&
                customerA.getId().contentEquals(customerB.getId())) return false;

        // If it's evaluating different customer types then unique values do not repeat each other
        if (!customerA.getType().contentEquals(customerB.getType())) return false;

        if (customerA.getType().contentEquals(constants.getCUSTOMER_PERSONAL_TYPE())) {
            // Unique value for Personal Accounts is Identity Number
            return customerA.getPersonDetails().getIdentityNumber().contentEquals(customerB.getPersonDetails().getIdentityNumber());

        } else if (customerA.getType().contentEquals(constants.getCUSTOMER_BUSINESS_TYPE())) {
            // Unique values for Business Accounts are RUC and Name
            return customerA.getBusinessDetails().getRuc().contentEquals(customerB.getBusinessDetails().getRuc()) ||
                    customerA.getBusinessDetails().getName().contentEquals(customerB.getBusinessDetails().getName());

        } else return false;
    }

    public Customer copyCustomer(Customer customerToBeCopied) {
        return new Customer(customerToBeCopied.getId(),
                customerToBeCopied.getType(),
                customerToBeCopied.getStatus(),
                customerToBeCopied.getPersonDetails(),
                customerToBeCopied.getBusinessDetails());
    }
}