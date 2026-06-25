package br.edu.ifpi.reserva_salas.validation;

import br.edu.ifpi.reserva_salas.dto.ReservaDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SalaAtivaStrategy implements ReservaValidationStrategy {

    private final SalaRepository salaRepository;

    @Override
    public void validate(ReservaDTO dto) {
        if (dto.getSalaId() == null) {
            return;
        }

        salaRepository.findById(dto.getSalaId()).ifPresent(sala -> {
            if (Boolean.FALSE.equals(sala.getAtiva())) {
                throw new RegraDeNegocioException(
                        "A sala '" + sala.getNome() + "' está inativa e não aceita reservas.");
            }
        });
    }
}