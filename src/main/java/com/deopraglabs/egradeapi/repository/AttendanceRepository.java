package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}
