package br.edu.ifpi.reserva_salas.controller;

import br.edu.ifpi.reserva_salas.dto.PerfilDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.model.Usuario;
import br.edu.ifpi.reserva_salas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioService usuarioService;


    @GetMapping
    public String verPerfil(Authentication auth, Model model) {
        Usuario usuario = usuarioService.buscarPorUsername(auth.getName());

        PerfilDTO dto = PerfilDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();

        model.addAttribute("perfilDTO", dto);
        model.addAttribute("usuario", usuario);
        return "perfil";
    }


    @PostMapping
    public String atualizarPerfil(@Valid @ModelAttribute("perfilDTO") PerfilDTO dto,
                                  BindingResult bindingResult,
                                  Authentication auth,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuarioService.buscarPorUsername(auth.getName()));
            return "perfil";
        }

        boolean trocouSenha = dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank();

        try {
            usuarioService.atualizarPerfil(auth.getName(), dto);
        } catch (RegraDeNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            model.addAttribute("usuario", usuarioService.buscarPorUsername(auth.getName()));
            return "perfil";
        }

        if (trocouSenha) {

            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Senha alterada com sucesso! Por favor, faça login novamente.");
            return "redirect:/logout";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Perfil atualizado com sucesso!");
        return "redirect:/perfil";
    }
}