package br.edu.ifpi.reserva_salas.service;

import br.edu.ifpi.reserva_salas.dto.PerfilDTO;
import br.edu.ifpi.reserva_salas.exception.RegraDeNegocioException;
import br.edu.ifpi.reserva_salas.model.Usuario;
import br.edu.ifpi.reserva_salas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));
    }


    public void atualizarPerfil(String username, PerfilDTO dto) {
        Usuario usuario = buscarPorUsername(username);


        usuario.setNome(dto.getNome().trim());
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            usuario.setEmail(dto.getEmail().trim());
        }


        boolean querTrocarSenha = dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank();
        if (querTrocarSenha) {
            if (dto.getSenhaAtual() == null || dto.getSenhaAtual().isBlank()) {
                throw new RegraDeNegocioException("Informe a senha atual para trocar a senha.");
            }
            if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getPassword())) {
                throw new RegraDeNegocioException("Senha atual incorreta.");
            }
            if (!dto.getNovaSenha().equals(dto.getConfirmacaoSenha())) {
                throw new RegraDeNegocioException("A nova senha e a confirmação não coincidem.");
            }
            if (dto.getNovaSenha().length() < 6) {
                throw new RegraDeNegocioException("A nova senha deve ter pelo menos 6 caracteres.");
            }
            usuario.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
        }

        usuarioRepository.save(usuario);
    }
}