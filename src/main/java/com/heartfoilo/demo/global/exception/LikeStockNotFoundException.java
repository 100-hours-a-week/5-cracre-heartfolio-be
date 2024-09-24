package com.heartfoilo.demo.global.exception;

import com.heartfoilo.demo.domain.stock.constant.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LikeStockNotFoundException extends RuntimeException{
    public LikeStockNotFoundException(String message) {
        super(message);
    }
}
