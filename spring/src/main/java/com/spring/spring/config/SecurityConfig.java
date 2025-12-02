package com.spring.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/error", "/css/**", "/js/**", 
                                 "/oauth2/authorization/**", "/login/oauth2/code/**",
                                 "/api/v1/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")                    //Página de login personalizada
                .defaultSuccessUrl("/students", true)   //Redireciona após login
                .failureUrl("/login?error")             //Redireciona se falhar
            )
            .logout(logout -> logout
                .logoutUrl("/logout")                   //Endpoint Spring para logout
                .logoutSuccessUrl("/google-logout")     //Redireciona para nosso controlador Google
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}