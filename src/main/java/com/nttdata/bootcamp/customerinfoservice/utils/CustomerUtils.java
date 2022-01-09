package com.nttdata.bootcamp.customerinfoservice.utils;

import com.nttdata.bootcamp.customerinfoservice.model.Customer;

public interface CustomerUtils {
    Boolean uniqueValuesDuplicity(Customer customerA, Customer customerB);
}
