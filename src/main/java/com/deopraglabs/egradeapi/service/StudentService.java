package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Gender;
import com.deopraglabs.egradeapi.model.Student;
import com.deopraglabs.egradeapi.model.Subject;
import com.deopraglabs.egradeapi.repository.CourseRepository;
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
import java.util.*;

@Service
@Slf4j
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    GradeRepository gradeRepository;

    public ResponseEntity<Student> login(Map<String, String> requestMap) {
        log.info("Logging in student {}");
        final Student student = studentRepository.findByCpf(requestMap.get("cpf"));
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
            if (Objects.isNull(studentRepository.findByCpf(requestMap.get("cpf")))) {
                if (Objects.isNull(studentRepository.findByEmail(requestMap.get("email")))) {
                    if (Objects.isNull(studentRepository.findByPhoneNumber(requestMap.get("phoneNumber")))) {
                        studentRepository.save(getStudentFromMap(requestMap));
                        return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
                    } else {
                        return EGradeUtils.getResponseEntity(Constants.PHONE_NUMBER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return EGradeUtils.getResponseEntity(Constants.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return EGradeUtils.getResponseEntity(Constants.CPF_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> delete(long id) {
        log.info("Deleting student by id {}", id);
        try {
            studentRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating student {}");
        try {
            final Student student = updateStudentFromMap(requestMap);
            studentRepository.save(student);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Student> findById(long id) {
        log.info("Finding student by id {}", id);
        try {
            final Optional<Student> teacher = studentRepository.findById(id);
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
        final Student student = new Student();

        student.setName(requestMap.get("name"));
        student.setCpf(requestMap.get("cpf"));
        student.setGender(Gender.valueOf(requestMap.get("gender")));
        student.setEmail(requestMap.get("email"));
        student.setPhoneNumber(requestMap.get("phoneNumber"));
        student.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        if (requestMap.get("password") != null) {
            student.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        }
        student.setActive(Boolean.parseBoolean(requestMap.get("active")));
        if (courseRepository.findById(Long.parseLong(requestMap.get("course"))).isPresent()) {
            student.setCourse(courseRepository.findById(Long.parseLong(requestMap.get("course"))).get());
        }
        if (requestMap.get("profilePicture") != null && !requestMap.get("profilePicture").isEmpty()) {
            student.setProfilePicture(requestMap.get("profilePicture"));
        }

        return student;
    }

    private Student updateStudentFromMap(Map<String, String> requestMap) throws ParseException {
        final Student student = studentRepository.findById(Long.parseLong(requestMap.get("id"))).get();

        student.setName(requestMap.get("name"));
        student.setCpf(requestMap.get("cpf"));
        student.setGender(Gender.valueOf(requestMap.get("gender")));
        student.setEmail(requestMap.get("email"));
        student.setPhoneNumber(requestMap.get("phoneNumber"));
        student.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        student.setActive(Boolean.parseBoolean(requestMap.get("active")));
        if (requestMap.get("password") != null && !requestMap.get("password").isEmpty()) {
            student.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        }
        if (courseRepository.findById(Long.parseLong(requestMap.get("course"))).isPresent()) {
            student.setCourse(courseRepository.findById(Long.parseLong(requestMap.get("course"))).get());
        }
        if (requestMap.get("profilePicture") != null && !requestMap.get("profilePicture").isEmpty()) {
            student.setProfilePicture(requestMap.get("profilePicture"));
        }

        return student;
    }

    public ResponseEntity<List<Student>> findAll() {
        log.info("Finding all students");
        try {
            return new ResponseEntity<>(studentRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<Student>> findAllByCourse(long courseId) {
        log.info("Finding all students by course");
        try {
            return new ResponseEntity<>(studentRepository.findAllByCourse(courseRepository.findById(courseId).get()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<List<Student>> findAllBySubject(long subjectId) {
        log.info("Finding all students by subject");
        try {
            Subject subject = subjectRepository.findById(subjectId).get();
            List<Student> studentList = new ArrayList<>();
            for (Student student : studentRepository.findAll()) {
                if (student.getCourse().getSubjects().contains(subject) && !studentList.contains(student)) {
                    studentList.add(student);
                }
            }
            return new ResponseEntity<>(studentList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Student> findByGrade(long gradeId) {
        log.info("Finding student by grade");
        try {
            return new ResponseEntity<>(studentRepository.findByGrades(gradeRepository.findById(gradeId).get()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
