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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
            coordinatorRepository.save(getCoordinatorFromMap(requestMap));
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> delete(Long id) {
        log.info("Deleting teacher by id {}", id);
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
            final Coordinator coordinator = getCoordinatorFromMap(requestMap);
            coordinator.setId(Long.parseLong(requestMap.get("id")));
            coordinatorRepository.save(coordinator);
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Coordinator> findById(Long id) {
        log.info("Finding teacher by id {}", id);
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
        Coordinator coordinator = new Coordinator();
        coordinator.setName(requestMap.get("name"));
        coordinator.setCpf(requestMap.get("cpf"));
        coordinator.setEmail(requestMap.get("email"));
        coordinator.setPhoneNumber(requestMap.get("phoneNumber"));
        coordinator.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        coordinator.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        coordinator.setActive(Boolean.parseBoolean(requestMap.get("active")));

        return coordinator;
    }
}
