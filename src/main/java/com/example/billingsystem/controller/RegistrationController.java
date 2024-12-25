package com.example.billingsystem.controller;

import com.example.billingsystem.entity.Adminstrator;
import com.example.billingsystem.model.LoginFormDTO;
import com.example.billingsystem.model.LoginResponseDTO;
import com.example.billingsystem.model.RegistrationFormDTO;
import com.example.billingsystem.service.AdministratorDetailService;
import com.example.billingsystem.service.RegAndLLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class RegistrationController {

@Autowired
  private AdministratorDetailService administratorDetailService;

@Autowired
private  RegAndLLoginService regAndLLoginService;


@PostMapping("/registration")// i said request body ok juju
    public ResponseEntity<?> registerAdmin(@RequestBody RegistrationFormDTO registrationFormDTO){
    return ResponseEntity.ok(regAndLLoginService.registerAdmin(registrationFormDTO));
}

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginFormDTO loginFormDTO){
    return ResponseEntity.ok(regAndLLoginService.login(loginFormDTO));
}

@GetMapping("/me")
    public ResponseEntity<?> me(){
    return ResponseEntity.ok("login works");
}





}
