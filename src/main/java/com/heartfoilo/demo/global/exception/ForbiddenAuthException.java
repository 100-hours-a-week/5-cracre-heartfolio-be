package com.heartfoilo.demo.global.exception;

import static com.heartfoilo.demo.global.constant.ExceptionMessage.FORBIDDEN_API_KEY_MSG;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenAuthException extends IllegalAccessException {

    public ForbiddenAuthException() {
        super(FORBIDDEN_API_KEY_MSG);
    }
}
