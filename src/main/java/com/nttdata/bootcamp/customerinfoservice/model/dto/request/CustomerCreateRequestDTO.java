package com.nttdata.bootcamp.customerinfoservice.model.dto.request;

import com.nttdata.bootcamp.customerinfoservice.model.BusinessDetails;
import com.nttdata.bootcamp.customerinfoservice.model.CustomerType;
import com.nttdata.bootcamp.customerinfoservice.model.PersonDetails;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerCreateRequestDTO {
    private CustomerType customerType;
    private PersonDetails personDetails;
    private BusinessDetails businessDetails;
}
