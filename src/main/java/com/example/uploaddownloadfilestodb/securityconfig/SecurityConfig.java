package com.example.uploaddownloadfilestodb.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler, PasswordEncoder passwordEncoder) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("admin")
                .password("$2a$10$3OzF/2DKs9PalLiVvbhsjuuWlb.q8TSpNplUIz98TDhtnaBZFNV7u")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("$2a$10$dlE0iRVU/1Sh6qRHNuM/k.2tbXyCJwsPF1ULjcYjqRRtadf4h5Vie")
                .roles("USER");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/files/delete").hasRole("ADMIN")
//                .antMatchers("/files/archive").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/files/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()//.authenticated()//
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
//                .permitAll()
//                .defaultSuccessUrl("/files")
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");

        http
                .csrf().disable()
                .cors().disable();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
