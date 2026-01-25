package br.com.arthur.api_wildex.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.arthur.api_wildex.dto.UsuarioFichaDTO;
import br.com.arthur.api_wildex.dto.UsuarioLoginDTO;
import br.com.arthur.api_wildex.dto.UsuarioLoginResponseDTO;
import br.com.arthur.api_wildex.dto.UsuarioGravaResponseDTO;
import br.com.arthur.api_wildex.dto.UsuarioPublicDTO;
import br.com.arthur.api_wildex.model.Usuario;
import br.com.arthur.api_wildex.service.UsuarioService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import br.com.arthur.api_wildex.security.JwtService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //construtor
    public UsuarioController (UsuarioService service, JwtService jwtService ,AuthenticationManager authenticationManager ) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    //metodos
    @PostMapping("/grava")
    public ResponseEntity<UsuarioGravaResponseDTO> criar (@RequestBody Usuario usuario) {

        Usuario criado = service.criar(usuario);

        UsuarioGravaResponseDTO response = new UsuarioGravaResponseDTO(
            criado.getId(),
            criado.getUsername(),
            criado.getNome(),
            criado.getEmail(),
            criado.getPais(),
            criado.getDataNascimento(),
            criado.getRole(),
            criado.getStatus()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/ficha/{id}")
    public ResponseEntity<UsuarioFichaDTO> buscar(@PathVariable Long id) {

        UsuarioFichaDTO usuario = service.encontrarUsuario(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(usuario); // 200
    }

    @GetMapping("/pesquisa/{identificador}")
    public ResponseEntity<List<UsuarioPublicDTO>> pesquisar (@RequestParam String termo) {

        List<UsuarioPublicDTO> usuarios = service.buscarPorUsernameOuNome(termo);

        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletar (@PathVariable("id") Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualiza/{id}")
    public ResponseEntity<Usuario> atualizar (@PathVariable("id") Long id, @RequestBody Usuario dados ) {
        
        try{

            Usuario usuarioAtualizado = service.atualizarUsuario(id, dados);

            return ResponseEntity.ok(usuarioAtualizado);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.notFound().build();

        }
    }

    @PostMapping("/logon")
    public ResponseEntity<UsuarioLoginResponseDTO> logar(
            @RequestBody UsuarioLoginDTO dto) {

        try {

            Authentication authToken =
                new UsernamePasswordAuthenticationToken(
                    dto.login(),
                    dto.senha()
                );

            Authentication authentication =
                authenticationManager.authenticate(authToken);

            Usuario usuario = (Usuario) authentication.getPrincipal();

            // Token
            String token = jwtService.gerarToken(usuario);

            UsuarioLoginResponseDTO response =
                new UsuarioLoginResponseDTO(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getRole(),
                    usuario.getStatus(),
                    token
                );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {

            // 401 = não autenticado
            return ResponseEntity.status(401).build();
        }
    }

}
