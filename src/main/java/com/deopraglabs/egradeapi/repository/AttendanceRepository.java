package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Attendance;
import com.deopraglabs.egradeapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    public Optional<Attendance> findById(Long id);

    public List<Attendance> findAll();

    List<Attendance> findByStudent(Student student);
}
