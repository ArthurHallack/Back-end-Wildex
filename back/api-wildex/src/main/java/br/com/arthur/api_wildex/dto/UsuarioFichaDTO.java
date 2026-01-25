package br.com.arthur.api_wildex.dto;

import java.time.LocalDateTime;

public class UsuarioFichaDTO {
    private Long id;
    private String username;
    private String nome;
    private String email;
    private String pais;
    private String fotoPerfil;
    private String bio;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UsuarioFichaDTO(
        Long id,
        String username,
        String nome,
        String email,
        String pais,
        String fotoPerfil,
        String bio,
        String role,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.email = email;
        this.pais = pais;
        this.fotoPerfil = fotoPerfil;
        this.bio = bio;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPais() {
        return pais;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public String getBio() {
        return bio;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
