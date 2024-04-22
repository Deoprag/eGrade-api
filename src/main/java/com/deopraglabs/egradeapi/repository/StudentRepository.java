package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    public Optional<Student> findById(Long id);

    public Student findByEmail(String email);

    public Student findByCpf(String cpf);

    public Student findByPhoneNumber(String phoneNumber);

    public List<Student> findAll();

}
