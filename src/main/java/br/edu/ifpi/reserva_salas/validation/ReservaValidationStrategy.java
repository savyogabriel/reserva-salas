package br.edu.ifpi.reserva_salas.validation;

import br.edu.ifpi.reserva_salas.dto.ReservaDTO;


public interface ReservaValidationStrategy {


    void validate(ReservaDTO dto);
}