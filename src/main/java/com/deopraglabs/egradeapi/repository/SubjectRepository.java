package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
