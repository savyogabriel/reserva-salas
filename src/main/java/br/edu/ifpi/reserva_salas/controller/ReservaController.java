package br.edu.ifpi.reserva_salas.controller;

import br.edu.ifpi.reserva_salas.dto.ReservaDTO;
import br.edu.ifpi.reserva_salas.model.StatusReserva;
import br.edu.ifpi.reserva_salas.service.ReservaService;
import br.edu.ifpi.reserva_salas.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final SalaService salaService;






    @GetMapping
    public String listar(@RequestParam(required = false) StatusReserva status,
                         Authentication auth,
                         Model model) {

        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isAdmin) {

            var reservas = status != null
                    ? reservaService.listarPorStatus(status)
                    : reservaService.listarTodas();
            model.addAttribute("reservas", reservas);
        } else {

            model.addAttribute("reservas",
                    reservaService.listarPorUsuario(auth.getName()));
        }

        model.addAttribute("statusFiltro", status);
        model.addAttribute("statusValues", StatusReserva.values());
        model.addAttribute("isAdmin", isAdmin);
        return "reserva/lista";
    }






    @GetMapping("/solicitar")
    public String formSolicitar(Model model) {
        model.addAttribute("reservaDTO", new ReservaDTO());
        model.addAttribute("salas", salaService.listarAtivas());
        return "reserva/form";
    }


    @PostMapping("/solicitar")
    public String solicitar(@Valid @ModelAttribute("reservaDTO") ReservaDTO dto,
                            BindingResult bindingResult,
                            Authentication auth,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("salas", salaService.listarAtivas());
            return "reserva/form";
        }


        dto.setNomeUsuario(auth.getName());

        reservaService.solicitar(dto);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Reserva solicitada com sucesso! Aguarde aprovação do administrador.");
        return "redirect:/reservas";
    }






    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservaService.cancelar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Reserva cancelada com sucesso.");
        return "redirect:/reservas";
    }






    @GetMapping("/aprovacao")
    public String paginaAprovacao(Model model) {
        model.addAttribute("reservas",
                reservaService.listarPorStatus(StatusReserva.PENDENTE));
        return "reserva/aprovacao";
    }


    @PostMapping("/{id}/aprovar")
    public String aprovar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservaService.aprovar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Reserva aprovada com sucesso!");
        return "redirect:/reservas/aprovacao";
    }


    @PostMapping("/{id}/rejeitar")
    public String rejeitar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reservaService.rejeitar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Reserva rejeitada.");
        return "redirect:/reservas/aprovacao";
    }


    @Controller
    static class LoginController {
        @GetMapping("/login")
        public String login() {
            return "login";
        }
    }
}