package com.example.billingsystem.service;

import com.example.billingsystem.entity.Adminstrator;
import com.example.billingsystem.jwt.JwtUtils;
import com.example.billingsystem.model.LoginFormDTO;
import com.example.billingsystem.model.LoginResponseDTO;
import com.example.billingsystem.model.RegistrationFormDTO;
import com.example.billingsystem.repository.AdminRepository;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegAndLLoginService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtUtils jwtUtils;

    public String registerAdmin(RegistrationFormDTO admin){
        if(adminRepository.findByPhoneNumber(admin.phoneNumber).isPresent()){

            return "Phone number already exists in the database";

        }
        Adminstrator adminstrator = new Adminstrator();
        adminstrator.setFullName(admin.fullName);
        adminstrator.setPhoneNumber(admin.phoneNumber);
        adminstrator.setEmail(admin.email);
        adminstrator.setGarageName(admin.garageName);
        adminstrator.setGarageAddress(admin.garageAddress);
        adminstrator.setPassword(passwordEncoder.encode(admin.password));
        adminRepository.save(adminstrator);

        return "Admin registered successfully";
    }

    public Map<String, Object> login(LoginFormDTO login){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.phoneNumber, login.password));
        }catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status","false");

            return map; // idhi bad login ki status false vastadhi
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails =(UserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();
        LoginResponseDTO responseDTO = new LoginResponseDTO(userDetails.getUsername(),roles,jwt);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Login suc" +
                "" +
                "cessful");
        map.put("jwt",responseDTO.getJwtToken());
        map.put("status","true");


        return map; // idhemo correct
    }


}
