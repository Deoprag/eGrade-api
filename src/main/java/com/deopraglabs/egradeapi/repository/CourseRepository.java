package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    public Optional<Course> findById(Long id);

    public Course findByName(String name);

    public List<Course> findAll();
}
