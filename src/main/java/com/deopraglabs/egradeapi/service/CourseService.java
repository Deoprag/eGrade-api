package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Course;
import com.deopraglabs.egradeapi.model.Subject;
import com.deopraglabs.egradeapi.repository.CoordinatorRepository;
import com.deopraglabs.egradeapi.repository.CourseRepository;
import com.deopraglabs.egradeapi.repository.SubjectRepository;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CoordinatorRepository coordinatorRepository;

    @Autowired
    SubjectRepository subjectRepository;

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering course {}");
        try {
            courseRepository.save(getCourseFromMap(requestMap));
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> delete(Long id) {
        log.info("Deleting course by id {}", id);
        try {
            courseRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating course {}");
        try {
            final Course course = getCourseFromMap(requestMap);
            course.setId(Long.parseLong(requestMap.get("id")));
            courseRepository.save(course);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Course> findById(Long id) {
        log.info("Finding course by id {}", id);
        try {
            final Optional<Course> course = courseRepository.findById(id);
            return new ResponseEntity<>(course.get(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Course(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Course getCourseFromMap(Map<String, String> requestMap) throws ParseException {
        final Course course = new Course();

        course.setName(requestMap.get("name"));
        course.setDescription(requestMap.get("description"));
        course.setCoordinator(coordinatorRepository.findById(Long.parseLong(requestMap.get("coordinator_id"))).get());
        course.setSubjects(getSubjects(requestMap.get("subjects")));

        return course;
    }

    private List<Subject> getSubjects(String subjects) {
        final List<Subject> subjectList = new ArrayList<>();
        final String[] subjectIds = subjects.split(",");

        for (String subjectId : subjectIds) {
            subjectList.add(subjectRepository.findById(Long.parseLong(subjectId)).get());
        }

        return subjectList;
    }
}