package br.edu.ifpi.reserva_salas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaDTO {


    private Long id;

    @NotBlank(message = "O nome da sala é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Min(value = 1, message = "A capacidade mínima é 1 pessoa")
    private Integer capacidade;

    private Boolean ativa;
}