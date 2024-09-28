package com.heartfoilo.demo.domain.fortune.exception;

public class DuplicateRegistDailyFortune extends RuntimeException {

    public DuplicateRegistDailyFortune(String message){
        super(message);
    }
}
