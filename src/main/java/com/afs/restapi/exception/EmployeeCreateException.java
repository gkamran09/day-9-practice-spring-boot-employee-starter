package com.afs.restapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmployeeCreateException extends RuntimeException {

    public EmployeeCreateException(String message) {
        super(message);
    }
}
