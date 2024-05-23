package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Grade;
import com.deopraglabs.egradeapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    public Optional<Grade> findById(Long id);

    public List<Grade> findAll();

    List<Grade> findByStudent(Student student);
}
