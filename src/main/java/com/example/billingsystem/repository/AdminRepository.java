package com.example.billingsystem.repository;

import com.example.billingsystem.entity.Adminstrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Adminstrator,Integer> {

    Optional<Adminstrator> findByPhoneNumber(String phoneNumber);

}
