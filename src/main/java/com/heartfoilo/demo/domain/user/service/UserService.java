package com.heartfoilo.demo.domain.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


    public ResponseEntity<?> getInfo(long userId);
}
