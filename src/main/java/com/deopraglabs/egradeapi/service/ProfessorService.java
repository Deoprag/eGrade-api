package com.deopraglabs.egradeapi.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.deopraglabs.egradeapi.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deopraglabs.egradeapi.model.Professor;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    public ResponseEntity<Professor> login(Map<String, String> requestMap) {
        log.info("Logging in professor {}");
        final Professor professor = professorRepository.findByCpf(requestMap.get("cpf"));
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
            if (Objects.isNull(professorRepository.findByCpf(requestMap.get("cpf")))) {
                if (Objects.isNull(professorRepository.findByEmail(requestMap.get("email")))) {
                    if (Objects.isNull(professorRepository.findByPhoneNumber(requestMap.get("phoneNumber")))) {
                        professorRepository.save(getProfessorFromMap(requestMap));
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
        log.info("Deleting professor by id {}", id);
        try {
            professorRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating professor {}");
        try {
            final Optional<Professor> optProfessor = professorRepository.findById(Long.parseLong(requestMap.get("id")));
            if (optProfessor.isPresent()) {
                professorRepository.save(updateProfessorFromMap(requestMap, optProfessor.get()));
                return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
            } else {
                return EGradeUtils.getResponseEntity(Constants.NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Professor> findById(long id) {
        log.info("Finding professor by id {}", id);
        try {
            final Optional<Professor> teacher = professorRepository.findById(id);
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

    private Professor getProfessorFromMap(Map<String, String> requestMap) throws Exception {
        final Professor professor = new Professor();

        professor.setName(requestMap.get("name"));
        professor.setCpf(requestMap.get("cpf"));
        professor.setEmail(requestMap.get("email"));
        professor.setPhoneNumber(requestMap.get("phoneNumber"));
        professor.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        professor.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        professor.setActive(Boolean.parseBoolean(requestMap.get("active")));

        return professor;
    }

    private Professor updateProfessorFromMap(Map<String, String> requestMap, Professor professor) throws Exception {
        if (requestMap.get("name") != null) professor.setName(requestMap.get("name"));
        if (requestMap.get("cpf") != null) professor.setCpf(requestMap.get("cpf"));
        if (requestMap.get("email") != null) professor.setEmail(requestMap.get("email"));
        if (requestMap.get("phoneNumber") != null) professor.setPhoneNumber(requestMap.get("phoneNumber"));
        if (requestMap.get("birthDate") != null)
            professor.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        if (requestMap.get("profilePicture") != null)
            professor.setProfilePicture(requestMap.get("profilePicture").getBytes());
        if (requestMap.get("active") != null) professor.setActive(Boolean.parseBoolean(requestMap.get("active")));

        return professor;
    }

    public ResponseEntity<List<Professor>> findAll() {
        log.info("Finding all professors");
        try {
            final List<Professor> professors = professorRepository.findAll();
            return new ResponseEntity<>(professors, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
