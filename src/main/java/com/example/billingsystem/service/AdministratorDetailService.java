package com.example.billingsystem.service;

import com.example.billingsystem.entity.Adminstrator;
import com.example.billingsystem.jwt.JwtUtils;
import com.example.billingsystem.model.LoginFormDTO;
import com.example.billingsystem.model.LoginResponseDTO;
import com.example.billingsystem.model.RegistrationFormDTO;
import com.example.billingsystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Adminstrator admin = adminRepository.findByPhoneNumber(username).orElseThrow(()->new UsernameNotFoundException("Admin not found with phone number: "+ username));

        return admin;
    }

    public String registerAdmin(RegistrationFormDTO admin){
        if(adminRepository.findByPhoneNumber(admin.phoneNumber).isPresent()){

            return "Phone number already exists in the database";

        }
        Adminstrator adminstrator = new Adminstrator();
        adminstrator.setFullName(admin.fullName);
        adminstrator.setPhoneNumber(admin.phoneNumber);
        adminstrator.setGarageName(admin.garageName);
        adminstrator.setGarageAddress(admin.garageAddress);
        adminstrator.setPassword(passwordEncoder.encode(admin.password));
        adminRepository.save(adminstrator);

        return "Admin registered successfully";
    }

   public String login(LoginFormDTO login){
       Authentication authentication;
      try {
           authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(login.phoneNumber, login.password));
      }catch (AuthenticationException e) {
          Map<String, Object> map = new HashMap<>();
          map.put("message", "Bad Credentials");
          map.put("status","false");

            return map.toString();
      }
       SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetails userDetails =(UserDetails) authentication.getPrincipal();

       String jwt = jwtUtils.generateTokenFromUsername(userDetails);

       List<String> roles = userDetails.getAuthorities().stream()
               .map(item -> item.getAuthority())
               .toList();
       LoginResponseDTO responseDTO = new LoginResponseDTO(userDetails.getUsername(),roles,jwt);
return responseDTO.toString() ;
    }
}
