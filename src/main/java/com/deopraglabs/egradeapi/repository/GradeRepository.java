package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    public Optional<Grade> findById(Long id);

    public List<Grade> findAll();
}
