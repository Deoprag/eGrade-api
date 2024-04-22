package com.deopraglabs.egradeapi.serviceImpl;

import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deopraglabs.egradeapi.model.Professor;
import com.deopraglabs.egradeapi.service.ProfessorService;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    com.deopraglabs.egradeapi.repository.ProfessorRepository teacherRepository;

    public ResponseEntity<Professor> login(Map<String, String> requestMap) {
        log.info("Logging in professor {}");
        final Professor professor = teacherRepository.findByCpf(requestMap.get("cpf"));
        if (professor != null) {
            if (Objects.equals(EGradeUtils.hashPassword(requestMap.get("password")), professor.getPassword())) {
                return new ResponseEntity<>(professor, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Professor(), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(new Professor(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering professor {}");
        try {
            teacherRepository.save(getProfessorFromMap(requestMap));
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
        log.info("Updating professor {}");
        try {
            final Professor professor = getProfessorFromMap(requestMap);
            professor.setId(Long.parseLong(requestMap.get("id")));
            teacherRepository.save(professor);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Professor> findById(Long id) {
        log.info("Finding teacher by id {}", id);
        try {
            final Optional<Professor> teacher = teacherRepository.findById(id);
            if (teacher.isPresent()) {
                return new ResponseEntity<>(teacher.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Professor(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Professor(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Professor getProfessorFromMap(Map<String, String> requestMap) throws ParseException {
        Professor professor = new Professor();
        professor.setName(requestMap.get("name"));
        professor.setCpf(requestMap.get("cpf"));
        professor.setEmail(requestMap.get("email"));
        professor.setPhoneNumber(requestMap.get("phoneNumber"));
        professor.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        professor.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        professor.setActive(Boolean.parseBoolean(requestMap.get("active")));

        return professor;
    }
}
