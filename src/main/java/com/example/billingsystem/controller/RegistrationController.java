package com.example.billingsystem.controller;

import com.example.billingsystem.entity.Adminstrator;
import com.example.billingsystem.model.LoginFormDTO;
import com.example.billingsystem.model.LoginResponseDTO;
import com.example.billingsystem.model.RegistrationFormDTO;
import com.example.billingsystem.service.AdministratorDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RegistrationController {
@Autowired
  private AdministratorDetailService administratorDetailService;


@PostMapping("/registration")// i said request body ok juju
    public ResponseEntity<?> registerAdmin(@RequestBody RegistrationFormDTO registrationFormDTO){
    return ResponseEntity.ok(administratorDetailService.registerAdmin(registrationFormDTO));
}

public ResponseEntity<?> login(@RequestBody LoginFormDTO loginFormDTO){
    return ResponseEntity.ok(administratorDetailService.login(loginFormDTO));
}





}
