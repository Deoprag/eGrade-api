package com.deopraglabs.egradeapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.deopraglabs.egradeapi.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deopraglabs.egradeapi.model.Subject;
import com.deopraglabs.egradeapi.service.SubjectService;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;


@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody() Map<String, String> requestMap) {
        try {
            return subjectService.save(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody() Map<String, String> requestMap) {
        try {
            return subjectService.update(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            return subjectService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAll/")
    public ResponseEntity<List<Subject>> findAll() {
        try {
            return subjectService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Subject> findById(@PathVariable Long id) {
        try {
            return subjectService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Subject(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
