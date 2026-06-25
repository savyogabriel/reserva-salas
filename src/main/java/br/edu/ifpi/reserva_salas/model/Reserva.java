package br.edu.ifpi.reserva_salas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name = "reservas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;


    @Column(name = "nome_usuario", nullable = false, length = 100)
    private String nomeUsuario;


    @Column(nullable = false)
    private LocalDate data;


    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;


    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusReserva status = StatusReserva.PENDENTE;


    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;


    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;


    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusReserva.PENDENTE;
        }
    }
}