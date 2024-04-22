package com.deopraglabs.egradeapi.serviceImpl;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deopraglabs.egradeapi.model.Professor;
import com.deopraglabs.egradeapi.repository.TeacherRepository;
import com.deopraglabs.egradeapi.service.TeacherService;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService{
    
    @Autowired
    TeacherRepository teacherRepository; 

    public ResponseEntity<Professor> login(Map<String, String> requestMap) {
        log.info("Logging in teacher {}");
        final Professor professor = teacherRepository.findByCpf(requestMap.get("cpf"));
        if(professor != null) {
            if(EGradeUtils.hashPassword(requestMap.get("password")).equals(professor.getPassword())) {
                return new ResponseEntity<Professor>(professor, HttpStatus.OK);
            } else {
                return new ResponseEntity<Professor>(new Professor(), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<Professor>(new Professor(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering teacher {}");
        try {
            teacherRepository.save(getTeacherFromMap(requestMap));
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

    public ResponseEntity<Professor> findById(Long id) {
        log.info("Finding teacher by id {}", id);
        try {
            final Optional<Professor> teacher = teacherRepository.findById(id);
            if(teacher.get() != null) {
                return new ResponseEntity<Professor>(teacher.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<Professor>(new Professor(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Professor>(new Professor(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Professor getTeacherFromMap(Map<String, String> requestMap) throws ParseException {
        Professor professor = new Professor();
        professor.setName(requestMap.get("name"));
        professor.setCpf(requestMap.get("cpf"));
        professor.setEmail(requestMap.get("email"));
        professor.setPhoneNumber(requestMap.get("phoneNumber"));
        professor.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        professor.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        professor.setActive(true);
        return professor;
    }
}
