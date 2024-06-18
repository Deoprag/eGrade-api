package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Course;
import com.deopraglabs.egradeapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    public Optional<Student> findById(Long id);

    public Student findByEmail(String email);

    public Student findByCpf(String cpf);

    public Student findByPhoneNumber(String phoneNumber);

    public List<Student> findAll();

    public List<Student> findAllByCourse(Course course);
}
