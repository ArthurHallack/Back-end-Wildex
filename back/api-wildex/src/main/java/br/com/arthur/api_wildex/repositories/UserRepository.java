package br.com.arthur.api_wildex.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arthur.api_wildex.model.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {

    // ---------------------------
    // BUSCA POR CAMPOS ÚNICOS
    // ---------------------------
    //Optional (um tipo que: se for encontrado guarda o obj se não fica vazio ou seja != de null )
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsername(String username);

    // ---------------------------
    // BUSCAS COMUNS
    // ---------------------------
    List<Usuario> findByStatus(String status);

    List<Usuario> findByRole(String role);

    List<Usuario> findByPais(String pais);

    // Buscar por nome exato
    List<Usuario> findByNome(String nome);

    // Buscar nomes que contenham algo (LIKE %nome%)
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    // ---------------------------
    // BUSCAS COM MULTIPLOS CRITÉRIOS
    // ---------------------------
    Optional<Usuario> findByEmailAndSenha(String email, String senha);

    List<Usuario> findByStatusAndRole(String status, String role);

    // ---------------------------
    // BUSCA POR TELEFONE
    // ---------------------------
    Optional<Usuario> findByTelefone(String telefone);

    // ---------------------------
    // BUSCAS POR DATA
    // ---------------------------
    List<Usuario> findByDataNascimento(LocalDate dataNascimento);

    List<Usuario> findByDataNascimentoBefore(LocalDate date);

    List<Usuario> findByDataNascimentoAfter(LocalDate date);

    // ---------------------------
    // VERIFICADORES (existence checks)
    // ---------------------------
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    //----------------------------
    // DTO
    //----------------------------

    List<Usuario> findByUsernameContainingIgnoreCaseOrNomeContainingIgnoreCase(
        String username,
        String nome
    );
}
