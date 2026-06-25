package br.edu.ifpi.reserva_salas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "O username é obrigatório")
    private String username;


    @Column(nullable = false)
    private String password;


    @Column(nullable = false, length = 100)
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100)
    private String nome;


    @Column(length = 150)
    @Email(message = "E-mail inválido")
    private String email;


    @Column(nullable = false, length = 20)
    @Builder.Default
    private String role = "USER";

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;
}