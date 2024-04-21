package com.example.paydaytrade.exceptions;

import com.example.paydaytrade.enums.Exceptions;

public class ApplicationException extends RuntimeException{

    private final Exceptions exceptions;

    public ApplicationException(Exceptions exceptions){
        super(exceptions.getMessage());
        this.exceptions = exceptions;
    }

}
