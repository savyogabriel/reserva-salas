package br.edu.ifpi.reserva_salas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "salas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "O nome da sala é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;


    @Column(nullable = false)
    @Min(value = 1, message = "A capacidade mínima é 1 pessoa")
    private Integer capacidade;


    @Column(nullable = false)
    @Builder.Default
    private Boolean ativa = true;
}