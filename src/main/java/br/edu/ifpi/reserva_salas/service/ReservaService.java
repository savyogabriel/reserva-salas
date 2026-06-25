package br.edu.ifpi.reserva_salas.service;

import br.edu.ifpi.reserva_salas.dto.ReservaDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.exception.ReservaNotFoundException;
import br.edu.ifpi.reserva_salas.model.Reserva;
import br.edu.ifpi.reserva_salas.model.Sala;
import br.edu.ifpi.reserva_salas.model.StatusReserva;
import br.edu.ifpi.reserva_salas.repository.ReservaRepository;
import br.edu.ifpi.reserva_salas.repository.SalaRepository;
import br.edu.ifpi.reserva_salas.validation.ReservaValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;


    private final List<ReservaValidationStrategy> validationStrategies;






    @Transactional(readOnly = true)
    public List<Reserva> listarTodas() {
        return reservaRepository.findAllByOrderByDataDescHoraInicioDesc();
    }


    @Transactional(readOnly = true)
    public List<Reserva> listarPorStatus(StatusReserva status) {
        return reservaRepository.findByStatusOrderByDataAscHoraInicioAsc(status);
    }


    @Transactional(readOnly = true)
    public List<Reserva> listarPorUsuario(String nomeUsuario) {
        return reservaRepository.findByNomeUsuarioOrderByDataDescHoraInicioDesc(nomeUsuario);
    }


    @Transactional(readOnly = true)
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNotFoundException(
                        "Reserva com ID " + id + " não encontrada."));
    }






    public Reserva solicitar(ReservaDTO dto) {

        validationStrategies.forEach(strategy -> strategy.validate(dto));

        Sala sala = salaRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new RegraDeNegocioException("Sala não encontrada."));

        Reserva reserva = Reserva.builder()
                .sala(sala)
                .nomeUsuario(dto.getNomeUsuario())
                .data(dto.getData())
                .horaInicio(dto.getHoraInicio())
                .horaFim(dto.getHoraFim())
                .status(StatusReserva.PENDENTE)
                .build();

        return reservaRepository.save(reserva);
    }


    public Reserva aprovar(Long id) {
        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != StatusReserva.PENDENTE) {
            throw new RegraDeNegocioException(
                    "Apenas reservas PENDENTES podem ser aprovadas. " +
                    "Status atual: " + reserva.getStatus());
        }

        reserva.setStatus(StatusReserva.APROVADA);
        return reservaRepository.save(reserva);
    }


    public Reserva rejeitar(Long id) {
        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() != StatusReserva.PENDENTE) {
            throw new RegraDeNegocioException(
                    "Apenas reservas PENDENTES podem ser rejeitadas. " +
                    "Status atual: " + reserva.getStatus());
        }

        reserva.setStatus(StatusReserva.REJEITADA);
        return reservaRepository.save(reserva);
    }


    public Reserva cancelar(Long id) {
        Reserva reserva = buscarPorId(id);

        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new RegraDeNegocioException("Esta reserva já foi cancelada.");
        }
        if (reserva.getStatus() == StatusReserva.REJEITADA) {
            throw new RegraDeNegocioException("Reservas rejeitadas não podem ser canceladas.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        reserva.setDataCancelamento(LocalDateTime.now());
        return reservaRepository.save(reserva);
    }
}