package com.deopraglabs.egradeapi.service;

import com.deopraglabs.egradeapi.model.Coordinator;
import com.deopraglabs.egradeapi.repository.CoordinatorRepository;
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
public class CoordinatorService {

    @Autowired
    CoordinatorRepository coordinatorRepository;

    public ResponseEntity<Coordinator> login(Map<String, String> requestMap) {
        log.info("Logging in coordinator {}");
        final Coordinator coordinator = coordinatorRepository.findByCpf(requestMap.get("cpf"));
        if (coordinator != null) {
            if (Objects.equals(EGradeUtils.hashPassword(requestMap.get("password")), coordinator.getPassword())) {
                return new ResponseEntity<>(coordinator, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Coordinator(), HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(new Coordinator(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering coordinator {}");
        try {
            if (Objects.isNull(coordinatorRepository.findByCpf(requestMap.get("cpf")))) {
                if (Objects.isNull(coordinatorRepository.findByEmail(requestMap.get("email")))) {
                    if (Objects.isNull(coordinatorRepository.findByPhoneNumber(requestMap.get("phoneNumber")))) {
                        coordinatorRepository.save(getCoordinatorFromMap(requestMap));
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
        log.info("Deleting coordinator by id {}", id);
        try {
            coordinatorRepository.deleteById(id);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(Map<String, String> requestMap) {
        log.info("Updating coordinator {}");
        try {
            final Coordinator coordinator = updateCoordinatorFromMap(requestMap);
            coordinatorRepository.save(coordinator);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Coordinator> findById(long id) {
        log.info("Finding coordinator by id {}", id);
        try {
            final Optional<Coordinator> teacher = coordinatorRepository.findById(id);
            if (teacher.isPresent()) {
                return new ResponseEntity<>(teacher.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Coordinator(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Coordinator(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Coordinator getCoordinatorFromMap(Map<String, String> requestMap) throws ParseException {
        final Coordinator coordinator = new Coordinator();

        coordinator.setName(requestMap.get("name"));
        coordinator.setCpf(requestMap.get("cpf"));
        coordinator.setEmail(requestMap.get("email"));
        coordinator.setPhoneNumber(requestMap.get("phoneNumber"));
        coordinator.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        coordinator.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        coordinator.setActive(Boolean.parseBoolean(requestMap.get("active")));
        if (requestMap.get("profilePicture") != null) {
            coordinator.setProfilePicture(requestMap.get("profilePicture"));
        }

        return coordinator;
    }

    private Coordinator updateCoordinatorFromMap(Map<String, String> requestMap) throws ParseException {
        final Coordinator coordinator = coordinatorRepository.findById(Long.parseLong(requestMap.get("id"))).get();

        coordinator.setName(requestMap.get("name"));
        coordinator.setCpf(requestMap.get("cpf"));
        coordinator.setEmail(requestMap.get("email"));
        coordinator.setPhoneNumber(requestMap.get("phoneNumber"));
        coordinator.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        coordinator.setActive(Boolean.parseBoolean(requestMap.get("active")));
        if (requestMap.get("password") != null && !requestMap.get("password").isEmpty()) {
            coordinator.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        }
        if (requestMap.get("profilePicture") != null) {
            coordinator.setProfilePicture(requestMap.get("profilePicture"));
        }

        return coordinator;
    }

    public ResponseEntity<List<Coordinator>> findAll() {
        log.info("Finding all coordinators");
        try {
            final List<Coordinator> coordinators = coordinatorRepository.findAll();
            return new ResponseEntity<>(coordinators, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
