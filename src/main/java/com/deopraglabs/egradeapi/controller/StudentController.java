package com.deopraglabs.egradeapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.deopraglabs.egradeapi.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deopraglabs.egradeapi.model.Student;
import com.deopraglabs.egradeapi.service.StudentService;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;


@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody() Map<String, String> requestMap) {
        try {
            return studentService.save(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody() Map<String, String> requestMap) {
        try {
            return studentService.update(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            return studentService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Student> findById(@PathVariable Long id) {
        try {
            return studentService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Student(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Student>> findById() {
        try {
            return studentService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
