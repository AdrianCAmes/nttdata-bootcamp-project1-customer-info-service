package com.nttdata.bootcamp.customerinfoservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerType {
    private String group;
    private String subgroup;
}