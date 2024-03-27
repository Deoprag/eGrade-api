package com.deopraglabs.egradeapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deopraglabs.egradeapi.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);

    public User findByCpf(String cpf);

    public User findByPhoneNumber(String phoneNumber);

    public List<User> findAll();

}