package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Grade;
import com.deopraglabs.egradeapi.model.Student;
import com.deopraglabs.egradeapi.repository.GradeRepository;
import com.deopraglabs.egradeapi.repository.StudentRepository;
import com.deopraglabs.egradeapi.repository.SubjectRepository;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class GradeService {

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    StudentRepository studentRepository;

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering grade {}");
        try {
            gradeRepository.save(getGradeFromMap(requestMap));
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> delete(long id) {
        log.info("Deleting grade by id {}", id);
        try {
            gradeRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating grade {}");
        try {
            final Grade grade = updateGradeFromMap(requestMap);
            gradeRepository.save(grade);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Grade> findById(long id) {
        log.info("Finding grade by id {}", id);
        try {
            final Optional<Grade> grade = gradeRepository.findById(id);
            return new ResponseEntity<>(grade.get(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Grade(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Grade getGradeFromMap(Map<String, String> requestMap) throws ParseException {
        final Grade grade = new Grade();

        grade.setId(Long.parseLong(requestMap.get("id")));
        grade.setSubject(subjectRepository.findById(Long.parseLong(requestMap.get("subject_id"))).get());
        grade.setStudent(studentRepository.findById(Long.parseLong(requestMap.get("student_id"))).get());
        grade.setN1(Float.parseFloat(requestMap.get("n1")));
        grade.setN2(Float.parseFloat(requestMap.get("n2")));
        if(requestMap.get("test1") != null && !requestMap.get("test1").isEmpty()) {
            grade.setTest1(requestMap.get("test1"));
        }
        if(requestMap.get("test2") != null && !requestMap.get("test2").isEmpty()) {
            grade.setTest1(requestMap.get("test2"));
        }

        return grade;
    }


    private Grade updateGradeFromMap(Map<String, String> requestMap) throws ParseException {
        final Grade grade = new Grade();

        grade.setSubject(subjectRepository.findById(Long.parseLong(requestMap.get("subject_id"))).get());
        grade.setStudent(studentRepository.findById(Long.parseLong(requestMap.get("student_id"))).get());
        grade.setN1(Float.parseFloat(requestMap.get("n1")));
        grade.setN2(Float.parseFloat(requestMap.get("n2")));
        if(requestMap.get("test1") != null && !requestMap.get("test1").isEmpty()) {
            grade.setTest1(requestMap.get("test1"));
        }
        if(requestMap.get("test2") != null && !requestMap.get("test2").isEmpty()) {
            grade.setTest1(requestMap.get("test2"));
        }

        return grade;
    }

    public ResponseEntity<List<Grade>> findByStudentId(long studentId) {
        log.info("Finding grades by student id {}", studentId);
        try {
            final Optional<Student> student = studentRepository.findById(studentId);

            if (student.isPresent()) {
                return new ResponseEntity<>(gradeRepository.findByStudent(student.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
