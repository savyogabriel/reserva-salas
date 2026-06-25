package br.edu.ifpi.reserva_salas.validation;

import br.edu.ifpi.reserva_salas.dto.ReservaDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ConflitoDePeriodoStrategy implements ReservaValidationStrategy {

    private final ReservaRepository reservaRepository;

    @Override
    public void validate(ReservaDTO dto) {

        if (dto.getHoraFim() != null && dto.getHoraInicio() != null
                && !dto.getHoraFim().isAfter(dto.getHoraInicio())) {
            throw new RegraDeNegocioException(
                    "A hora de fim deve ser posterior à hora de início.");
        }


        if (dto.getSalaId() != null && dto.getData() != null
                && dto.getHoraInicio() != null && dto.getHoraFim() != null) {

            var conflitos = reservaRepository.findConflitos(
                    dto.getSalaId(),
                    dto.getData(),
                    dto.getHoraInicio(),
                    dto.getHoraFim()
            );

            if (!conflitos.isEmpty()) {
                throw new RegraDeNegocioException(
                        "Já existe uma reserva aprovada para esta sala no período informado. " +
                        "Escolha outro horário ou sala.");
            }
        }
    }
}