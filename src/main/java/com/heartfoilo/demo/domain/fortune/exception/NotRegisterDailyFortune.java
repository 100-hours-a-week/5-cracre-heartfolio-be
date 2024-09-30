package com.heartfoilo.demo.domain.fortune.exception;

import static com.heartfoilo.demo.domain.fortune.constant.ErrorMessage.NOT_CONTEXT_MSG;

import javax.naming.NotContextException;

public class NotRegisterDailyFortune extends NotContextException {

    public NotRegisterDailyFortune(){
        super(NOT_CONTEXT_MSG);
    }
}
