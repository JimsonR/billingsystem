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

    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "jwtToken='" + jwtToken + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }

    private List<String> roles;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
