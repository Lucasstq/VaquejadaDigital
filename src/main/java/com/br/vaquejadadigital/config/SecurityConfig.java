package com.br.vaquejadadigital.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        //Endpoints de autenticação públicos
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        //Endpoints publicos de eventos e senhas
                        .requestMatchers(HttpMethod.GET, "/api/eventos/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/eventos/ativos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/eventos/{id}/senhas/mapa").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        //Endpoints de ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/eventos").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/eventos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/eventos/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/vendas/manual").hasAuthority("ADMIN")
                        //Endpoints de juiz e locutor
                        .requestMatchers("/api/rodizios/**").hasAnyAuthority("ADMIN", "JUIZ", "LOCUTOR")
                        .requestMatchers("/api/resultados/**").hasAnyAuthority("ADMIN", "JUIZ")
                        .requestMatchers("/api/ordem-corrida/**").hasAnyAuthority("ADMIN", "JUIZ", "LOCUTOR")
                        .anyRequest().authenticated()

                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
