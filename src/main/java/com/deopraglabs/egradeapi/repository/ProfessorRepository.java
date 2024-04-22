package com.deopraglabs.egradeapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deopraglabs.egradeapi.model.Professor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    public Optional<Professor> findById(Long id);

    public Professor findByEmail(String email);

    public Professor findByCpf(String cpf);

    public Professor findByPhoneNumber(String phoneNumber);

    public List<Professor> findAll();

}