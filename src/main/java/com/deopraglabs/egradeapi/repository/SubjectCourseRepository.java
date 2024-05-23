package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Course;
import com.deopraglabs.egradeapi.model.SubjectCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectCourseRepository extends JpaRepository<SubjectCourse, Long> {

    public Optional<SubjectCourse> findById(Long id);

    public List<SubjectCourse> findAll();
}
