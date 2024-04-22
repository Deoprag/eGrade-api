package com.deopraglabs.egradeapi.controller;

import java.util.Date;
import java.util.Map;

import com.deopraglabs.egradeapi.model.Professor;
import com.deopraglabs.egradeapi.repository.TeacherRepository;
import com.deopraglabs.egradeapi.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    @Autowired
    TeacherService teacherService;

    @Autowired
    TeacherRepository teacherRepository;

     @PostMapping("")
     public ResponseEntity<Professor> login(@RequestBody(required = true) Map<String, String> requestMap) {
         try {
            return teacherService.login(requestMap);
         } catch (Exception e) {
             return new ResponseEntity<Professor>(new Professor(), HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

     @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody(required = true) Map<String, String> requestMap) {
            try {
                for (int i = 0; i < 10; i++) {
                    Professor professor = new Professor();
                    professor.setName("Professor " + i);
                    professor.setCpf("1234567890" + i);
                    professor.setEmail("professor" + i + "@gmail.com");
                    professor.setPhoneNumber("9876543210" + i);
                    professor.setBirthDate(new Date());
                    professor.setPassword(EGradeUtils.hashPassword("senha" + i));
                    professor.setActive(true);

                    teacherRepository.save(professor);
                }
                return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
            } catch (Exception e) {
                return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
