package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {

}
