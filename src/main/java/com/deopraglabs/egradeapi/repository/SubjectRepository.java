package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    public Optional<Subject> findById(Long id);

    public List<Subject> findAll();

    List<Subject> findByCourseId(Long courseId);
}
