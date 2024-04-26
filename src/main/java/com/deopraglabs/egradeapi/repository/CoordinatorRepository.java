package com.deopraglabs.egradeapi.repository;

import com.deopraglabs.egradeapi.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

    public Optional<Coordinator> findById(Long id);

    public Coordinator findByEmail(String email);

    public Coordinator findByCpf(String cpf);

    public Coordinator findByPhoneNumber(String phoneNumber);

    public List<Coordinator> findAll();
}
