package com.heartfoilo.demo.global.exception;


import static com.heartfoilo.demo.global.constant.ExceptionMessage.NO_AUTH_MSG;

import javax.naming.AuthenticationException;

public class NoAuthInfoException extends AuthenticationException {

    public NoAuthInfoException() {
        super(NO_AUTH_MSG);
    }


}
