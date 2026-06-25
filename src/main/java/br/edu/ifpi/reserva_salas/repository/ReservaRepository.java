package br.edu.ifpi.reserva_salas.repository;

import br.edu.ifpi.reserva_salas.model.Reserva;
import br.edu.ifpi.reserva_salas.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {


    @Query("""
        SELECT r FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.data = :data
          AND r.status = 'APROVADA'
          AND r.horaInicio < :horaFim
          AND r.horaFim > :horaInicio
        """)
    List<Reserva> findConflitos(
            @Param("salaId") Long salaId,
            @Param("data") LocalDate data,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim
    );


    List<Reserva> findByStatusOrderByDataAscHoraInicioAsc(StatusReserva status);


    List<Reserva> findByNomeUsuarioOrderByDataDescHoraInicioDesc(String nomeUsuario);


    List<Reserva> findAllByOrderByDataDescHoraInicioDesc();
}