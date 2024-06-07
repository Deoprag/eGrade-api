package com.deopraglabs.egradeapi.controller;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import com.deopraglabs.egradeapi.model.Coordinator;
import com.deopraglabs.egradeapi.model.Gender;
import com.deopraglabs.egradeapi.model.Professor;
import com.deopraglabs.egradeapi.model.Role;
import com.deopraglabs.egradeapi.repository.CoordinatorRepository;
import com.deopraglabs.egradeapi.repository.ProfessorRepository;
import com.deopraglabs.egradeapi.repository.StudentRepository;
import com.deopraglabs.egradeapi.service.ProfessorService;
import com.deopraglabs.egradeapi.service.StudentService;
import com.deopraglabs.egradeapi.service.CoordinatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    EGradeUtils eGradeUtils;

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody() Map<String, String> requestMap) {
        try {
            Role role = eGradeUtils.getRole(requestMap.get("cpf"));
            return switch (role) {
                case COORDENADOR -> coordinatorService.login(requestMap);
                case PROFESSOR -> professorService.login(requestMap);
                case ALUNO -> studentService.login(requestMap);
                default -> new ResponseEntity<>(Constants.INVALID_DATA, HttpStatus.NOT_FOUND);
            };
        } catch (Exception e) {
            return new ResponseEntity<>(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/registerProfessor")
    public ResponseEntity<String> registerProfessor() {
        try {
            for (int i = 1001; i < 1010; i++) {
                Professor professor = new Professor();
                professor.setName("Professor " + i);
                professor.setCpf("6543210" + i);
                professor.setEmail("professor" + i + "@gmail.com");
                professor.setGender(Gender.M);
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

    @PostMapping("/registerCoordinator")
    public ResponseEntity<String> registerCoordinator() {
        try {
            for (int i = 1001; i < 1010; i++) {
                Coordinator coordinator = new Coordinator();
                coordinator.setName("Coordinator " + i);
                coordinator.setCpf("0123456" + i);
                coordinator.setEmail("coordinator" + i + "@gmail.com");
                coordinator.setGender(Gender.M);
                coordinator.setPhoneNumber("9876" + i);
                coordinator.setBirthDate(new Date());
                coordinator.setPassword(EGradeUtils.hashPassword("senha" + i));
                coordinator.setActive(true);

                coordinatorRepository.save(coordinator);
            }
            return EGradeUtils.getResponseEntity(Constants.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {
            return EGradeUtils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
