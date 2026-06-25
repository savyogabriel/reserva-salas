package br.edu.ifpi.reserva_salas.controller;

import br.edu.ifpi.reserva_salas.dto.SalaDTO;
import br.edu.ifpi.reserva_salas.model.Sala;
import br.edu.ifpi.reserva_salas.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService salaService;






    @GetMapping
    public String listar(Model model) {
        model.addAttribute("salas", salaService.listarTodas());
        return "sala/lista";
    }






    @GetMapping("/cadastrar")
    public String formCadastrar(Model model) {
        model.addAttribute("salaDTO", new SalaDTO());
        model.addAttribute("modoEdicao", false);
        return "sala/form";
    }


    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute("salaDTO") SalaDTO dto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            return "sala/form";
        }

        salaService.cadastrar(dto);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Sala '" + dto.getNome() + "' cadastrada com sucesso!");
        return "redirect:/salas";
    }






    @GetMapping("/{id}/editar")
    public String formEditar(@PathVariable Long id, Model model) {
        Sala sala = salaService.buscarPorId(id);

        SalaDTO dto = SalaDTO.builder()
                .id(sala.getId())
                .nome(sala.getNome())
                .capacidade(sala.getCapacidade())
                .ativa(sala.getAtiva())
                .build();

        model.addAttribute("salaDTO", dto);
        model.addAttribute("modoEdicao", true);
        return "sala/form";
    }


    @PostMapping("/{id}/editar")
    public String editar(@PathVariable Long id,
                         @Valid @ModelAttribute("salaDTO") SalaDTO dto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modoEdicao", true);
            return "sala/form";
        }

        salaService.atualizar(id, dto);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Sala '" + dto.getNome() + "' atualizada com sucesso!");
        return "redirect:/salas";
    }






    @PostMapping("/{id}/inativar")
    public String inativar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Sala sala = salaService.buscarPorId(id);
        salaService.inativar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Sala '" + sala.getNome() + "' inativada com sucesso.");
        return "redirect:/salas";
    }






    @PostMapping("/{id}/reativar")
    public String reativar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Sala sala = salaService.buscarPorId(id);
        salaService.reativar(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso",
                "Sala '" + sala.getNome() + "' reativada com sucesso!");
        return "redirect:/salas";
    }
}