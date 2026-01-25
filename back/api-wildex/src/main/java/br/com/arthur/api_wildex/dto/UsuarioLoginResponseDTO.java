package br.com.arthur.api_wildex.dto;

public record UsuarioLoginResponseDTO(
    Long id,
    String username,
    String nome,
    String email,
    String role,
    String status,
    String token
) {}
