package br.edu.ifpi.reserva_salas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    @NotNull(message = "Selecione uma sala")
    private Long salaId;


    private String nomeUsuario;

    @NotNull(message = "A data da reserva é obrigatória")
    @Future(message = "A data deve ser no futuro")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    @NotNull(message = "A hora de início é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaInicio;

    @NotNull(message = "A hora de fim é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaFim;
}