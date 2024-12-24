package com.example.billingsystem.model;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDTO {

    public LoginResponseDTO(String username, List<String> roles,String jwtToken){
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }

    private String jwtToken;

    private String username;

    private List<String> roles;


}
