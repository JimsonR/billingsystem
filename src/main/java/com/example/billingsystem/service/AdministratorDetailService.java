package com.example.billingsystem.service;

import com.example.billingsystem.entity.Adminstrator;
import com.example.billingsystem.jwt.JwtUtils;
import com.example.billingsystem.model.LoginFormDTO;
import com.example.billingsystem.model.LoginResponseDTO;
import com.example.billingsystem.model.RegistrationFormDTO;
import com.example.billingsystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AdministratorDetailService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Adminstrator admin = adminRepository.findByPhoneNumber(username).orElseThrow(()->new UsernameNotFoundException("Admin not found with phone number: "+ username));

        return admin;
    }



}
