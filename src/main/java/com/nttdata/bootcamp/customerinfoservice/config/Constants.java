package com.nttdata.bootcamp.customerinfoservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.customer.personal_group}")
    private String CUSTOMER_PERSONAL_GROUP;

    @Value("${constants.customer.business_group}")
    private String CUSTOMER_BUSINESS_GROUP;
}
