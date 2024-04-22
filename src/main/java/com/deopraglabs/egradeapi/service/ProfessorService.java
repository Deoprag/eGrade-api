package com.deopraglabs.egradeapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deopraglabs.egradeapi.model.Professor;

@Service
public interface ProfessorService {

    public ResponseEntity<Professor> login(Map<String, String> requestMap);
    
    public ResponseEntity<String> save(Map<String, String> requestMap);

    public ResponseEntity<String> delete(Long id);

    public ResponseEntity<Professor> findById(Long id);

    public ResponseEntity<String> update(Map<String, String> requestMap);

}
