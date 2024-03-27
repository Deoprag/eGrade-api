package com.deopraglabs.egradeapi.serviceImpl;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deopraglabs.egradeapi.model.Role;
import com.deopraglabs.egradeapi.model.User;
import com.deopraglabs.egradeapi.repository.UserRepository;
import com.deopraglabs.egradeapi.service.UserService;
import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    
    @Autowired
    UserRepository userRepository; 

    private User getUserFromMap(Map<String, String> requestMap) throws ParseException {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setEmail(requestMap.get("email"));
        user.setRole(Role.valueOf(requestMap.get("role")));
        user.setBirthDate(EGradeUtils.stringToDate(requestMap.get("birthDate")));
        user.setCpf(requestMap.get("cpf"));
        user.setPhoneNumber(requestMap.get("phoneNumber"));
        user.setPassword(EGradeUtils.hashPassword(requestMap.get("password")));
        return user;
    }

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering user {}");
        try {
            userRepository.save(getUserFromMap(requestMap));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
