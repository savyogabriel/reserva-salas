package br.edu.ifpi.reserva_salas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()


                .requestMatchers(
                    "/salas/cadastrar",
                    "/salas/*/editar",
                    "/salas/*/inativar",
                    "/salas/*/reativar"
                ).hasRole("ADMIN")
                .requestMatchers(
                    "/reservas/aprovacao/**",
                    "/reservas/*/aprovar",
                    "/reservas/*/rejeitar"
                ).hasRole("ADMIN")


                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}