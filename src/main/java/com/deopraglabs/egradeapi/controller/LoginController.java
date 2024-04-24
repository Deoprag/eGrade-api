package com.deopraglabs.egradeapi.controller;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import com.deopraglabs.egradeapi.model.Professor;
import com.deopraglabs.egradeapi.model.Role;
import com.deopraglabs.egradeapi.repository.CoordinatorRepository;
import com.deopraglabs.egradeapi.repository.ProfessorRepository;
import com.deopraglabs.egradeapi.repository.StudentRepository;
import com.deopraglabs.egradeapi.service.ProfessorService;
import com.deopraglabs.egradeapi.service.StudentService;
import com.deopraglabs.egradeapi.service.CoordinatorService;
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

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CoordinatorService coordinatorService;

    @Autowired
    CoordinatorRepository coordinatorRepository;

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody() Map<String, String> requestMap) {
        try {
            Role role = EGradeUtils.getRole(requestMap.get("cpf"));

            return switch (role) {
                case PROFESSOR -> professorService.login(requestMap);
                case ALUNO -> studentService.login(requestMap);
                case COORDENADOR -> coordinatorService.login(requestMap);
                default -> new ResponseEntity<>(Constants.INVALID_DATA, HttpStatus.NOT_FOUND);
            };
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register() {
        try {
            for (int i = 1000001; i < 2000011; i++) {
                Professor professor = new Professor();
                professor.setName("Professor " + i);
                professor.setCpf("1234" + i);
                professor.setEmail("professor" + i + "@gmail.com");
                professor.setPhoneNumber("9876" + i);
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
