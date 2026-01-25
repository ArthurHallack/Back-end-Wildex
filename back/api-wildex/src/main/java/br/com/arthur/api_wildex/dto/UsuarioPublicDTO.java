package br.com.arthur.api_wildex.dto;

public class UsuarioPublicDTO {

    private Long id;
    private String username;
    private String nome;

    public UsuarioPublicDTO(Long id, String username, String nome) {
        this.id = id;
        this.username = username;
        this.nome = nome;
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
}
