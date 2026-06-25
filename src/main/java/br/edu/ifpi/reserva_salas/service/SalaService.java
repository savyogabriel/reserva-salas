package br.edu.ifpi.reserva_salas.service;

import br.edu.ifpi.reserva_salas.dto.SalaDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.exception.SalaNotFoundException;
import br.edu.ifpi.reserva_salas.model.Sala;
import br.edu.ifpi.reserva_salas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class SalaService {

    private final SalaRepository salaRepository;






    @Transactional(readOnly = true)
    public List<Sala> listarTodas() {
        return salaRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Sala> listarAtivas() {
        return salaRepository.findByAtivaTrue();
    }


    @Transactional(readOnly = true)
    public Sala buscarPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new SalaNotFoundException(
                        "Sala com ID " + id + " não encontrada."));
    }






    public Sala cadastrar(SalaDTO dto) {
        validarNomeUnico(dto.getNome(), null);

        Sala sala = Sala.builder()
                .nome(dto.getNome().trim())
                .capacidade(dto.getCapacidade())
                .ativa(true)
                .build();

        return salaRepository.save(sala);
    }


    public Sala atualizar(Long id, SalaDTO dto) {
        Sala sala = buscarPorId(id);


        validarNomeUnico(dto.getNome(), id);

        sala.setNome(dto.getNome().trim());
        sala.setCapacidade(dto.getCapacidade());

        return salaRepository.save(sala);
    }


    public void inativar(Long id) {
        Sala sala = buscarPorId(id);
        if (!sala.getAtiva()) {
            throw new RegraDeNegocioException("A sala já está inativa.");
        }
        sala.setAtiva(false);
        salaRepository.save(sala);
    }


    public void reativar(Long id) {
        Sala sala = buscarPorId(id);
        if (sala.getAtiva()) {
            throw new RegraDeNegocioException("A sala já está ativa.");
        }
        sala.setAtiva(true);
        salaRepository.save(sala);
    }






    private void validarNomeUnico(String nome, Long idAtual) {
        salaRepository.findByNomeIgnoreCase(nome.trim()).ifPresent(existente -> {
            if (!existente.getId().equals(idAtual)) {
                throw new RegraDeNegocioException(
                        "Já existe uma sala com o nome '" + nome + "'.");
            }
        });
    }
}