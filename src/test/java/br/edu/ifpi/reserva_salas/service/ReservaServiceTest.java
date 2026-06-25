package br.edu.ifpi.reserva_salas.service;

import br.edu.ifpi.reserva_salas.dto.ReservaDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.exception.ReservaNotFoundException;
import br.edu.ifpi.reserva_salas.model.Reserva;
import br.edu.ifpi.reserva_salas.model.Sala;
import br.edu.ifpi.reserva_salas.model.StatusReserva;
import br.edu.ifpi.reserva_salas.repository.ReservaRepository;
import br.edu.ifpi.reserva_salas.repository.SalaRepository;
import br.edu.ifpi.reserva_salas.validation.ConflitoDePeriodoStrategy;
import br.edu.ifpi.reserva_salas.validation.ReservaValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService — Testes Unitários")
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private ConflitoDePeriodoStrategy conflitoDePeriodoStrategy;

    private ReservaService reservaService;

    private Sala sala;
    private ReservaDTO dto;
    private Reserva reservaPendente;

    @BeforeEach
    void setup() {

        List<ReservaValidationStrategy> strategies = List.of(conflitoDePeriodoStrategy);
        reservaService = new ReservaService(reservaRepository, salaRepository, strategies);

        sala = Sala.builder()
                .id(1L).nome("Sala 101").capacidade(20).ativa(true)
                .build();

        dto = ReservaDTO.builder()
                .salaId(1L)
                .nomeUsuario("joao")
                .data(LocalDate.now().plusDays(1))
                .horaInicio(LocalTime.of(9, 0))
                .horaFim(LocalTime.of(11, 0))
                .build();

        reservaPendente = Reserva.builder()
                .id(1L)
                .sala(sala)
                .nomeUsuario("joao")
                .data(dto.getData())
                .horaInicio(dto.getHoraInicio())
                .horaFim(dto.getHoraFim())
                .status(StatusReserva.PENDENTE)
                .dataCriacao(LocalDateTime.now())
                .build();
    }





    @Test
    @DisplayName("RN03 — reserva solicitada deve iniciar com status PENDENTE")
    void deveSolicitarReservaComStatusPendente() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(conflitoDePeriodoStrategy).validate(any());

        Reserva salva = reservaService.solicitar(dto);

        assertThat(salva.getStatus()).isEqualTo(StatusReserva.PENDENTE);
        assertThat(salva.getNomeUsuario()).isEqualTo("joao");
    }





    @Test
    @DisplayName("RN04 — deve lançar exceção quando strategy detectar conflito de horário")
    void deveLancarExcecaoQuandoHouverConflito() {
        doThrow(new RegraDeNegocioException("Conflito de horário detectado."))
                .when(conflitoDePeriodoStrategy).validate(any());

        assertThatThrownBy(() -> reservaService.solicitar(dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Conflito");

        verify(reservaRepository, never()).save(any());
    }





    @Test
    @DisplayName("RN05 — deve aprovar reserva com status PENDENTE")
    void deveAprovarReservaPendente() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaPendente));
        when(reservaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reserva aprovada = reservaService.aprovar(1L);

        assertThat(aprovada.getStatus()).isEqualTo(StatusReserva.APROVADA);
    }

    @Test
    @DisplayName("RN05 — deve lançar exceção ao aprovar reserva já aprovada")
    void deveLancarExcecaoAoAprovarReservaJaAprovada() {
        reservaPendente.setStatus(StatusReserva.APROVADA);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaPendente));

        assertThatThrownBy(() -> reservaService.aprovar(1L))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("APROVADA");
    }

    @Test
    @DisplayName("RN05 — deve lançar exceção ao aprovar reserva cancelada")
    void deveLancarExcecaoAoAprovarReservaCancelada() {
        reservaPendente.setStatus(StatusReserva.CANCELADA);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaPendente));

        assertThatThrownBy(() -> reservaService.aprovar(1L))
                .isInstanceOf(RegraDeNegocioException.class);
    }





    @Test
    @DisplayName("RN06 — cancelamento deve registrar dataCancelamento")
    void deveCancelarReservaERegistrarDataCancelamento() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaPendente));
        when(reservaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reserva cancelada = reservaService.cancelar(1L);

        assertThat(cancelada.getStatus()).isEqualTo(StatusReserva.CANCELADA);
        assertThat(cancelada.getDataCancelamento()).isNotNull();
        assertThat(cancelada.getDataCancelamento()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("RN06 — deve lançar exceção ao cancelar reserva já cancelada")
    void deveLancarExcecaoAoCancelarReservaJaCancelada() {
        reservaPendente.setStatus(StatusReserva.CANCELADA);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaPendente));

        assertThatThrownBy(() -> reservaService.cancelar(1L))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("cancelada");
    }





    @Test
    @DisplayName("deve lançar ReservaNotFoundException ao buscar ID inexistente")
    void deveLancarExcecaoQuandoReservaInexistente() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.buscarPorId(99L))
                .isInstanceOf(ReservaNotFoundException.class)
                .hasMessageContaining("99");
    }





    @Test
    @DisplayName("deve rejeitar reserva PENDENTE com sucesso")
    void deveRejeitarReservaPendente() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaPendente));
        when(reservaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reserva rejeitada = reservaService.rejeitar(1L);

        assertThat(rejeitada.getStatus()).isEqualTo(StatusReserva.REJEITADA);
    }
}