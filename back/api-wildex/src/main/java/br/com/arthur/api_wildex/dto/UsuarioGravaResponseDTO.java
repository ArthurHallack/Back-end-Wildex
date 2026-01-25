package br.com.arthur.api_wildex.dto;

import java.time.LocalDate;

public record UsuarioGravaResponseDTO(
    Long id,
    String username,
    String nome,
    String email,
    String pais,
    LocalDate dataNascimento,
    String role,
    String status
) {}