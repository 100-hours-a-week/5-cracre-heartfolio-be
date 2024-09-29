package com.heartfoilo.demo.global.api;

import com.heartfoilo.demo.domain.fortune.exception.DuplicateRegistDailyFortune;
import com.heartfoilo.demo.domain.fortune.exception.NotRegisterDailyFortune;
import com.heartfoilo.demo.global.dto.ResponseDto;
import com.heartfoilo.demo.global.exception.ForbiddenAuthException;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(NotFoundException exception){
        logging(exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().message(
            exception.getMessage()).build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDto> handleAuthException(AuthenticationException exception){
        logging(exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().message(
            exception.getMessage()).build());
    }

    @ExceptionHandler(DuplicateRegistDailyFortune.class)
    public ResponseEntity<ResponseDto> handleRuntimeException(RuntimeException exception){
        logging(exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.builder().message(
            exception.getMessage()).build());
    }

    @ExceptionHandler(NotRegisterDailyFortune.class)
    public ResponseEntity<ResponseDto> handleNoContentException(NotRegisterDailyFortune exception){
        logging(exception);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResponseDto.builder().message(
            exception.getMessage()).build());
    }

    private static void logging(Exception exception){
        log.warn("[ERROR] " + exception.getMessage());
    }
}
