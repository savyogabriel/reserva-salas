package br.edu.ifpi.reserva_salas.controller;

import br.edu.ifpi.reserva_salas.model.StatusReserva;
import br.edu.ifpi.reserva_salas.repository.ReservaRepository;
import br.edu.ifpi.reserva_salas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final SalaRepository salaRepository;
    private final ReservaRepository reservaRepository;


    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalSalas", salaRepository.count());
        model.addAttribute("salasAtivas", salaRepository.findByAtivaTrue().size());
        model.addAttribute("reservasPendentes",
                reservaRepository.findByStatusOrderByDataAscHoraInicioAsc(StatusReserva.PENDENTE).size());
        model.addAttribute("reservasAprovadas",
                reservaRepository.findByStatusOrderByDataAscHoraInicioAsc(StatusReserva.APROVADA).size());
        model.addAttribute("reservasCanceladas",
                reservaRepository.findByStatusOrderByDataAscHoraInicioAsc(StatusReserva.CANCELADA).size());
        return "dashboard";
    }
}