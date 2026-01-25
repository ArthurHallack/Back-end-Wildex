package br.com.arthur.api_wildex.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.arthur.api_wildex.model.Usuario;
import br.com.arthur.api_wildex.repositories.UserRepository;
import br.com.arthur.api_wildex.dto.UsuarioFichaDTO;
import br.com.arthur.api_wildex.dto.UsuarioPublicDTO;

@Service
public class UsuarioService {

    //Injeta dependência
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    //construtor
    public UsuarioService (UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    //metodos de negocios
    public Usuario criar(Usuario usuario) {

        // ---------- VALIDAÇÕES ----------
        //username
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username não pode ser vazio.");
        }

        if (usuario.getUsername().length() < 3 || usuario.getUsername().length() > 30) {
            throw new IllegalArgumentException("Username deve ter entre 3 e 30 caracteres.");
        }

        if (repository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Username já está em uso.");
        }
        //senha
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia.");
        }

        if (usuario.getSenha().length() < 6 || usuario.getSenha().length() > 255) {
            throw new IllegalArgumentException("Senha deve ter entre 6 e 255 caracteres.");
        }
        //nome
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }

        if (usuario.getNome().length() > 100 || usuario.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 100 caracteres.");
        }

        //email
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazia.");
        }

        if (usuario.getEmail().length() > 255) {
            throw new IllegalArgumentException("Email deve ter no maximo 255 caracteres.");
        }

        if (!usuario.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido.");
        }

        if (repository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }
        //pais

        if (usuario.getPais() != null && usuario.getPais().length() > 50) {
            throw new IllegalArgumentException("País inválido.");
        }
        //Bio

        if (usuario.getBio() != null && usuario.getBio().length() > 300) {
            throw new IllegalArgumentException("Bio pode ter no máximo 300 caracteres.");
        }
        //DataNascimento

        if (usuario.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento invalida.");
        }
        // ---------- SEGURANÇA ----------
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // ---------- SALVAR ----------
        return repository.save(usuario);
    }

    public void deletar(Long idUsuario) {

        Usuario usuario = repository.findById(idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    
        repository.delete(usuario);
    }
    
    public UsuarioFichaDTO encontrarUsuario(Long idUsuario){

            Usuario usuario = repository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

                return new UsuarioFichaDTO(
                    usuario.getId(),
                    usuario.getNickName(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getPais(),
                    usuario.getFotoPerfil(),
                    usuario.getBio(),
                    usuario.getRole(),
                    usuario.getStatus(),
                    usuario.getCreatedAt(),
                    usuario.getUpdatedAt()
                );
    }

    public List<UsuarioPublicDTO> buscarPorUsernameOuNome(String termo) {

        List<Usuario> usuarios = repository
            .findByUsernameContainingIgnoreCaseOrNomeContainingIgnoreCase(termo, termo);
    
        return usuarios.stream()
            .map(usuario -> new UsuarioPublicDTO(
                usuario.getId(),
                usuario.getNickName(),
                usuario.getNome()
            ))
            .toList();
    }

    public Usuario atualizarUsuario(Long id, Usuario dados) {

        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    
        usuario.setNome(dados.getNome());
        usuario.setBio(dados.getBio());
        usuario.setPais(dados.getPais());
    
        return repository.save(usuario);
    }
    
}