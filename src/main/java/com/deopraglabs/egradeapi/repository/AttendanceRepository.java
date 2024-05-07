package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    public Optional<Attendance> findById(Long id);

    public List<Attendance> findAll();

    List<Attendance> findByStudentId(long studentId);
}
