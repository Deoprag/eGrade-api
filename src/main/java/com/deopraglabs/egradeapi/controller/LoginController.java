package com.deopraglabs.egradeapi.controller;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import com.deopraglabs.egradeapi.model.Professor;
import com.deopraglabs.egradeapi.model.Role;
import com.deopraglabs.egradeapi.repository.ProfessorRepository;
import com.deopraglabs.egradeapi.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deopraglabs.egradeapi.util.Constants;
import com.deopraglabs.egradeapi.util.EGradeUtils;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    @Autowired
    ProfessorService professorService;

    @Autowired
    ProfessorRepository professorRepository;

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody() Map<String, String> requestMap) {
        try {
            return switch (Objects.requireNonNull(EGradeUtils.getRole(requestMap.get("cpf")))) {
                case Role.PROFESSOR -> professorService.login(requestMap);
                case Role.ALUNO -> studentService.login(requestMap);
                case Role.COORDENADOR -> coordinatorService.login(requestMap);
                default -> new ResponseEntity<>(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            };
        } catch (Exception e) {
            return new ResponseEntity<>(new Professor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register() {
        try {
            for (int i = 0; i < 10; i++) {
                Professor professor = new Professor();
                professor.setName("Professor " + i);
                professor.setCpf("1234567890" + i);
                professor.setEmail("professor" + i + "@gmail.com");
                professor.setPhoneNumber("9876543210" + i);
                professor.setBirthDate(new Date());
                professor.setPassword(EGradeUtils.hashPassword("senha" + i));
                professor.setActive(true);

                professorRepository.save(professor);
            }
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
