package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Course;
import com.deopraglabs.egradeapi.model.Student;
import com.deopraglabs.egradeapi.model.Subject;
import com.deopraglabs.egradeapi.repository.CoordinatorRepository;
import com.deopraglabs.egradeapi.repository.CourseRepository;
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
import java.util.ArrayList;
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

    @Autowired
    CourseRepository courseRepository;

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering subject {}");
        try {
            final Subject subject = subjectRepository.findByName(requestMap.get("name"));
            if (subject == null) {
                subjectRepository.save(getSubjectFromMap(requestMap));
                return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
            } else {
                return EGradeUtils.getResponseEntity(Constants.NAME_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
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

    public ResponseEntity<List<Subject>> findAll() {
        log.info("Finding all subjects {}");
        try {
            return new ResponseEntity<>(subjectRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<Subject>> findByCoordinatorId(long id) {
        log.info("Finding all subjects by coordinator id {}", id);
        try {
            courseRepository.findByCoordinator(coordinatorRepository.findById(id).get());
            List<Subject> subjectList = new ArrayList<>();
            for (Subject subject : subjectRepository.findAll()) {
                for (Course course : courseRepository.findByCoordinator(coordinatorRepository.findById(id).get())) {
                    if (course.getSubjects().contains(subject) && !subjectList.contains(subject)) {
                        subjectList.add(subject);
                    }
                }
            }
            return new ResponseEntity<>(subjectList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<Subject>> findByProfessorId(long id) {
        log.info("Finding all subjects by professor id {}", id);
        try {
            return new ResponseEntity<>(subjectRepository.findAllByProfessor(professorRepository.findById(id).get()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Subject getSubjectFromMap(Map<String, String> requestMap) throws ParseException {
        final Subject subject = new Subject();

        subject.setName(requestMap.get("name"));
        subject.setProfessor(professorRepository.findById(Long.parseLong(requestMap.get("professor_id"))).get());

        return subject;
    }
}
