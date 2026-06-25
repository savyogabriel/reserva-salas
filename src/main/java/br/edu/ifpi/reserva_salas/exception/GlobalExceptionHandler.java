package br.edu.ifpi.reserva_salas.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(RegraDeNegocioException.class)
    public String handleRegraDeNegocio(RegraDeNegocioException ex,
                                       RedirectAttributes redirectAttributes) {
        log.warn("Regra de negócio violada: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        return "redirect:/dashboard";
    }


    @ExceptionHandler(SalaNotFoundException.class)
    public String handleSalaNotFound(SalaNotFoundException ex, Model model) {
        log.warn("Sala não encontrada: {}", ex.getMessage());
        model.addAttribute("titulo", "Sala Não Encontrada");
        model.addAttribute("mensagem", ex.getMessage());
        return "error/error";
    }


    @ExceptionHandler(ReservaNotFoundException.class)
    public String handleReservaNotFound(ReservaNotFoundException ex, Model model) {
        log.warn("Reserva não encontrada: {}", ex.getMessage());
        model.addAttribute("titulo", "Reserva Não Encontrada");
        model.addAttribute("mensagem", ex.getMessage());
        return "error/error";
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, Model model) {
        List<String> erros = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.warn("Erros de validação: {}", erros);
        model.addAttribute("titulo", "Erro de Validação");
        model.addAttribute("mensagem", "Corrija os erros abaixo:");
        model.addAttribute("erros", erros);
        return "error/error";
    }


    @ExceptionHandler(Exception.class)
    public String handleGenerico(Exception ex, Model model) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        model.addAttribute("titulo", "Erro Interno");
        model.addAttribute("mensagem", "Ocorreu um erro inesperado. Tente novamente.");
        return "error/error";
    }
}