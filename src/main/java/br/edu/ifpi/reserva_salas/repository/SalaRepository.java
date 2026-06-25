package br.edu.ifpi.reserva_salas.repository;

import br.edu.ifpi.reserva_salas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {


    boolean existsByNomeIgnoreCase(String nome);


    Optional<Sala> findByNomeIgnoreCase(String nome);


    List<Sala> findByAtivaTrue();
}