package com.heartfoilo.demo.domain.user.service;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
@Service
public interface UserService {

    public ResponseEntity<?> getInfo(long userId);

}
