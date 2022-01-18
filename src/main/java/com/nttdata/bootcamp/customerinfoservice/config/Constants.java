package com.nttdata.bootcamp.customerinfoservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.customer.personal_group:}")
    private String customerPersonalGroup;

    @Value("${constants.customer.business_group:}")
    private String customerBusinessGroup;

    @Value("${constants.status.blocked:}")
    private String statusBlocked;

    @Value("${constants.status.active:}")
    private String statusActive;
}
