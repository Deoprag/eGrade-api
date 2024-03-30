package com.deopraglabs.egradeapi.serviceImpl;

import java.text.ParseException;
import java.util.Map;

import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient.ResponseSpec;

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

    public ResponseEntity<String> save(Map<String, String> requestMap) {
        log.info("Registering user {}");
        try {
            userRepository.save(getUserFromMap(requestMap));
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Logging in user {}");
        User user = userRepository.findByCpf(requestMap.get("cpf"));
        if(user != null) {
            if(EGradeUtils.hashPassword(requestMap.get("password")).equals(user.getPassword())) {
                return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
            } else {
                return EGradeUtils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.UNAUTHORIZED);
            }
        }
        return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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
}
