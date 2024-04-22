package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Student;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class StudentService {

    @Autowired
    com.deopraglabs.egradeapi.repository.StudentRepository teacherRepository;

    public ResponseEntity<Student> login(Map<String, String> requestMap) {
        log.info("Logging in student {}");
        final Student student = teacherRepository.findByCpf(requestMap.get("cpf"));
        if (student != null) {
            if (Objects.equals(EGradeUtils.hashPassword(requestMap.get("password")), student.getPassword())) {
                return new ResponseEntity<>(student, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Student(), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(new Student(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering student {}");
        try {
            teacherRepository.save(getStudentFromMap(requestMap));
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> delete(Long id) {
        log.info("Deleting teacher by id {}", id);
        try {
            teacherRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating student {}");
        try {
            final Student student = getStudentFromMap(requestMap);
            student.setId(Long.parseLong(requestMap.get("id")));
            teacherRepository.save(student);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Student> findById(Long id) {
        log.info("Finding teacher by id {}", id);
        try {
            final Optional<Student> teacher = teacherRepository.findById(id);
            if (teacher.isPresent()) {
                return new ResponseEntity<>(teacher.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Student(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Student(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Student getStudentFromMap(Map<String, String> requestMap) throws ParseException {
        Student student = new Student();
        student.setName(requestMap.get("name"));
        student.setCpf(requestMap.get("cpf"));
        student.setEmail(requestMap.get("email"));
        student.setPhoneNumber(requestMap.get("phoneNumber"));
        student.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        student.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        student.setActive(Boolean.parseBoolean(requestMap.get("active")));

        return student;
    }
}
