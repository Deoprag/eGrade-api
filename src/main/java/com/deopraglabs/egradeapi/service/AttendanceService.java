package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Attendance;
import com.deopraglabs.egradeapi.model.Student;
import com.deopraglabs.egradeapi.model.Subject;
import com.deopraglabs.egradeapi.repository.AttendanceRepository;
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
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    StudentRepository studentRepository;

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering attendance {}");
        try {
            attendanceRepository.save(getAttendanceFromMap(requestMap));
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> delete(long id) {
        log.info("Deleting attendance by id {}", id);
        try {
            attendanceRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating attendance {}");
        try {
            final Attendance attendance = getAttendanceFromMap(requestMap);
            attendance.setId(Long.parseLong(requestMap.get("id")));
            attendanceRepository.save(attendance);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Attendance> findById(long id) {
        log.info("Finding attendance by id {}", id);
        try {
            final Optional<Attendance> attendance = attendanceRepository.findById(id);
            return new ResponseEntity<>(attendance.get(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Attendance(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Attendance getAttendanceFromMap(Map<String, String> requestMap) throws ParseException {
        final Attendance attendance = new Attendance();

        attendance.setDate(Date.from(Instant.now()));
        attendance.setPresent(Boolean.parseBoolean(requestMap.get("present")));
        attendance.setSubject(subjectRepository.findById(Long.parseLong(requestMap.get("subjectId"))).get());
        attendance.setStudent(studentRepository.findById(Long.parseLong(requestMap.get("studentId"))).get());

        return attendance;
    }

    public ResponseEntity<List<Attendance>> findByStudentId(long studentId) {
        log.info("Finding attendance by student id {}", studentId);
        try {
            final Optional<Student> student = studentRepository.findById(studentId);

            if (student.isPresent()) {
                return new ResponseEntity<>(attendanceRepository.findByStudent(student.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<Attendance>> findBySubjectId(long subjectId) {
        log.info("Finding attendance by subjectId id {}", subjectId);
        try {
            final Optional<Subject> subject = subjectRepository.findById(subjectId);

            if (subject.isPresent()) {
                return new ResponseEntity<>(attendanceRepository.findBySubject(subject.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public ResponseEntity<List<Attendance>> findByDate(String date) {
        log.info("Finding attendance by subjectId date {}", date);
        try {
            return new ResponseEntity<>(attendanceRepository.findByDate(EGradeUtils.stringToDate2(date)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
