package com.example.billingsystem.config;

import com.example.billingsystem.entity.Adminstrator;
import com.example.billingsystem.jwt.AuthEntryPoint;
import com.example.billingsystem.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Autowired
    private DataSource dataSource;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors->{})
                .csrf((csrf)->csrf.disable())
                .authorizeHttpRequests((requests)->{
                 requests.requestMatchers("/","/auth/registration","/auth/login")   .permitAll()
                         .anyRequest().authenticated();

                });
        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(unauthorizedHandler));

        http.headers((headers)-> headers.frameOptions((frameOptionsConfig)->frameOptionsConfig.sameOrigin()));

        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){

            return new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}