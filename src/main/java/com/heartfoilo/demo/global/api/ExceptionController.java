package com.goorm.jeff.global.api;

import com.goorm.jeff.global.dto.ResponseDto;
import com.goorm.jeff.global.exception.ForbiddenAuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException exception){
        logging(exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.builder().message(
            exception.getMessage()).build());
    }

    @ExceptionHandler(ForbiddenAuthException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(IllegalAccessException exception){
        logging(exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.builder().message(
            exception.getMessage()).build());
    }

    private static void logging(Exception exception){
        log.warn("[ERROR] " + exception.getMessage());
    }
}
