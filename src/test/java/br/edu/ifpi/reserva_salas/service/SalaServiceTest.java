package br.edu.ifpi.reserva_salas.service;

import br.edu.ifpi.reserva_salas.dto.SalaDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.exception.SalaNotFoundException;
import br.edu.ifpi.reserva_salas.model.Sala;
import br.edu.ifpi.reserva_salas.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("SalaService — Testes Unitários")
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

    private SalaDTO dto;
    private Sala sala;

    @BeforeEach
    void setup() {
        dto = SalaDTO.builder()
                .nome("Sala 101")
                .capacidade(20)
                .build();

        sala = Sala.builder()
                .id(1L)
                .nome("Sala 101")
                .capacidade(20)
                .ativa(true)
                .build();
    }





    @Test
    @DisplayName("RN01 — deve lançar exceção ao cadastrar sala com nome duplicado")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        when(salaRepository.findByNomeIgnoreCase("Sala 101"))
                .thenReturn(Optional.of(sala));

        assertThatThrownBy(() -> salaService.cadastrar(dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Sala 101");
    }

    @Test
    @DisplayName("RN01 — deve permitir edição mantendo o mesmo nome da sala")
    void devePermitirEdicaoComMesmoNome() {

        when(salaRepository.findByNomeIgnoreCase("Sala 101"))
                .thenReturn(Optional.of(sala));
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);


        assertThatNoException().isThrownBy(() -> salaService.atualizar(1L, dto));
    }





    @Test
    @DisplayName("RN02 — sala deve ser cadastrada como ativa")
    void deveIniciarSalaComoAtiva() {
        when(salaRepository.findByNomeIgnoreCase("Sala 101")).thenReturn(Optional.empty());
        when(salaRepository.save(any(Sala.class))).thenAnswer(inv -> inv.getArgument(0));

        Sala salva = salaService.cadastrar(dto);

        assertThat(salva.getAtiva()).isTrue();
    }

    @Test
    @DisplayName("RN02 — deve cadastrar sala com sucesso quando nome for único")
    void deveCadastrarSalaComSucesso() {
        when(salaRepository.findByNomeIgnoreCase("Sala 101")).thenReturn(Optional.empty());
        when(salaRepository.save(any(Sala.class))).thenAnswer(inv -> inv.getArgument(0));

        Sala salva = salaService.cadastrar(dto);

        assertThat(salva.getNome()).isEqualTo("Sala 101");
        assertThat(salva.getCapacidade()).isEqualTo(20);
        verify(salaRepository, times(1)).save(any(Sala.class));
    }





    @Test
    @DisplayName("deve lançar SalaNotFoundException ao buscar ID inexistente")
    void deveLancarExcecaoQuandoSalaInexistente() {
        when(salaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> salaService.buscarPorId(99L))
                .isInstanceOf(SalaNotFoundException.class)
                .hasMessageContaining("99");
    }





    @Test
    @DisplayName("deve inativar sala com sucesso")
    void deveInativarSala() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class))).thenAnswer(inv -> inv.getArgument(0));

        salaService.inativar(1L);

        assertThat(sala.getAtiva()).isFalse();
        verify(salaRepository).save(sala);
    }

    @Test
    @DisplayName("deve listar apenas salas ativas")
    void deveListarSalasAtivas() {
        when(salaRepository.findByAtivaTrue()).thenReturn(List.of(sala));

        var ativas = salaService.listarAtivas();

        assertThat(ativas).hasSize(1);
        assertThat(ativas.get(0).getAtiva()).isTrue();
    }
}