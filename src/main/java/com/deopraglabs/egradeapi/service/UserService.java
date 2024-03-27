package com.deopraglabs.egradeapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public ResponseEntity<String> save(Map<String, String> requestMap);

}
