package com.goorm.jeff.global.exception;

import static com.goorm.jeff.global.constant.ExceptionMessage.NO_API_KEY_MSG;

import org.apache.coyote.BadRequestException;

public class NoAuthInfoException extends BadRequestException {

    public NoAuthInfoException() {
        super(NO_API_KEY_MSG);
    }


}
