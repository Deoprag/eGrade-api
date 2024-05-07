package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Subject;
import com.deopraglabs.egradeapi.repository.CoordinatorRepository;
import com.deopraglabs.egradeapi.repository.ProfessorRepository;
import com.deopraglabs.egradeapi.repository.SubjectRepository;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    CoordinatorRepository coordinatorRepository;

    @Autowired
    ProfessorRepository professorRepository;

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering subject {}");
        try {
            subjectRepository.save(getSubjectFromMap(requestMap));
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> delete(long id) {
        log.info("Deleting subject by id {}", id);
        try {
            subjectRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating subject {}");
        try {
            final Subject subject = getSubjectFromMap(requestMap);
            subject.setId(Long.parseLong(requestMap.get("id")));
            subjectRepository.save(subject);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Subject> findById(long id) {
        log.info("Finding subject by id {}", id);
        try {
            final Optional<Subject> subject = subjectRepository.findById(id);
            return new ResponseEntity<>(subject.get(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Subject(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Subject getSubjectFromMap(Map<String, String> requestMap) throws ParseException {
        final Subject subject = new Subject();

        subject.setName(requestMap.get("name"));
        subject.setProfessor(professorRepository.findById(Long.parseLong(requestMap.get("professor_id"))).get());

        return subject;
    }

    public ResponseEntity<List<Subject>> findByCourseId(long courseId) {
        log.info("Finding subject by course id {}", courseId);
        try {
            final List<Subject> subjects = subjectRepository.findByCourseId(courseId);
            return new ResponseEntity<>(subjects, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
