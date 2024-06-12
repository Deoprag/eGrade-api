package com.deopraglabs.egradeapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.deopraglabs.egradeapi.model.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deopraglabs.egradeapi.model.Course;
import com.deopraglabs.egradeapi.service.CourseService;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;


@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody() Map<String, String> requestMap) {
        try {
            return courseService.save(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody() Map<String, String> requestMap) {
        try {
            return courseService.update(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            return courseService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Course> findById(@PathVariable Long id) {
        try {
            return courseService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Course(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Course>> findById() {
        try {
            return courseService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/findByCoordinatorId/{id}")
    public ResponseEntity<List<Course>> findByCoordinatorId(@PathVariable long id) {
        try {
            return courseService.findByCoordinatorId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
