package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Course;
import com.deopraglabs.egradeapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    public Optional<Subject> findById(Long id);

    public Subject findByName(String name);

    public List<Subject> findAll();
}
