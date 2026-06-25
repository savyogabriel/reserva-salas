package br.edu.ifpi.reserva_salas.config;

import br.edu.ifpi.reserva_salas.model.Usuario;
import br.edu.ifpi.reserva_salas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        criarSeNaoExistir("admin", "admin123", "Administrador", "admin@ifpi.edu.br", "ADMIN");
        criarSeNaoExistir("joao",  "user123",  "João Silva",    "joao@ifpi.edu.br",  "USER");
        criarSeNaoExistir("maria", "user123",  "Maria Souza",   "maria@ifpi.edu.br", "USER");
        criarSeNaoExistir("pedro", "user123",  "Pedro Santos",  "pedro@ifpi.edu.br", "USER");
        log.info("Usuários padrão verificados/criados com sucesso.");
    }

    private void criarSeNaoExistir(String username, String senha, String nome,
                                   String email, String role) {
        if (!usuarioRepository.existsByUsername(username)) {
            var usuario = Usuario.builder()
                    .username(username)
                    .password(passwordEncoder.encode(senha))
                    .nome(nome)
                    .email(email)
                    .role(role)
                    .ativo(true)
                    .build();
            usuarioRepository.save(usuario);
            log.info("Usuário criado: {} [{}]", username, role);
        }
    }
}