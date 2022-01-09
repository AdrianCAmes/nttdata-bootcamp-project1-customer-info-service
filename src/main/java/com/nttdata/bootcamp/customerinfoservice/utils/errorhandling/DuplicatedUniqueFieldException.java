package com.nttdata.bootcamp.customerinfoservice.utils.errorhandling;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DuplicatedUniqueFieldException extends IllegalArgumentException {
    private static final long serialVersionUID = -5713584292717311038L;

    public DuplicatedUniqueFieldException(String s) {
        super(s);
    }
}
