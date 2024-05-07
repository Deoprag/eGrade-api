package com.deopraglabs.egradeapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deopraglabs.egradeapi.model.Attendance;
import com.deopraglabs.egradeapi.service.AttendanceService;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;


@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody() Map<String, String> requestMap) {
        try {
            return attendanceService.save(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody() Map<String, String> requestMap) {
        try {
            return attendanceService.update(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            return attendanceService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Attendance> findById(@PathVariable Long id) {
        try {
            return attendanceService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Attendance(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/findByStudentId/{id}")
    public ResponseEntity<List<Attendance>> findByStudentId(@PathVariable long id) {
        try {
            return attendanceService.findByStudentId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
