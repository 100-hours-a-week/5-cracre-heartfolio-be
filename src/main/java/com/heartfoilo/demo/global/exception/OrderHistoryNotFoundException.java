package com.heartfoilo.demo.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderHistoryNotFoundException extends RuntimeException{
    public OrderHistoryNotFoundException(String message) {
        super(message);
    }
}
