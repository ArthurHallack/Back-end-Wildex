package br.com.arthur.api_wildex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.arthur.api_wildex.dto.UsuarioFichaDTO;
import br.com.arthur.api_wildex.dto.UsuarioPublicDTO;
import br.com.arthur.api_wildex.model.Usuario;
import br.com.arthur.api_wildex.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UsuarioService service;

    //Testes relacionados a Att usuario
    @Test
    void usuarioNaoAtualizado () {

        Usuario dadosParaAtualizar = new Usuario(); // Pode estar vazio

        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT (Ação e Verificação)
        assertThrows(IllegalArgumentException.class, () -> {
            service.atualizarUsuario(1L, dadosParaAtualizar);
        });
        
        // Verificamos se o save NUNCA foi chamado (segurança extra)
        verify(repository, never()).save(any(Usuario.class));
        }
    
    @Test
    void usuarioAtualizado () {

        Usuario dadosParaAtualizar = new Usuario(); // Pode estar vazio
        Usuario dadosNovos = new Usuario(); // Pode estar vazio

        ReflectionTestUtils.setField(dadosParaAtualizar, "id", 1L);
        dadosParaAtualizar.setNome("Vegetto");
        dadosParaAtualizar.setBio("Yoshaaaaa");
        dadosParaAtualizar.setPais("Japão");

        ReflectionTestUtils.setField(dadosNovos, "id", 1L);
        dadosNovos.setNome("Gogeta");
        dadosNovos.setBio("Mudada");
        dadosNovos.setPais("Japão");

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(dadosParaAtualizar));
        Mockito.when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

        Usuario resultado = service.atualizarUsuario(1L, dadosNovos);

        verify(repository, times(1)).save(any(Usuario.class));
        
        assertEquals("Gogeta", resultado.getNome());
        assertEquals("Mudada", resultado.getBio());
        assertEquals(1L, resultado.getId());
    }

    //Testes relacionados a Deletar um usuario
    @Test
    void usuarioNaoDeletado () {

        Long idUsuario = 1L;

        Mockito.when(repository.findById(idUsuario)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->{
            service.deletar(idUsuario);
        });

        verify(repository, never()).delete(any());

    }

    @Test
    void usuarioDeletado () {

        Long idUsuario = 1L;
        Usuario UsuarioModelo = new Usuario();

        Mockito.when(repository.findById(idUsuario)).thenReturn(Optional.of(UsuarioModelo));

        service.deletar(idUsuario);

        verify(repository, times(1)).delete(UsuarioModelo);
    }

    @Test
    void usuarioNaoEncontrado () {

        Long idUsuario = 1L;

        Mockito.when(repository.findById(idUsuario)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->{
            service.encontrarUsuario(idUsuario);
        });

    }

    @Test
    void usuarioEncontrado () {

        Long idUsuario = 1L;
        Usuario usuarioModelo = new Usuario();
        usuarioModelo.setNickName("Vegetal123");
        usuarioModelo.setNome("Vegeta");
        usuarioModelo.setEmail("vegeta@sayajin.com");
        usuarioModelo.setPais("Japão");
        usuarioModelo.setFotoPerfil("");
        usuarioModelo.setBio("Sou o grande Vegetaaaaaa");
        usuarioModelo.setRole("user");
        usuarioModelo.setStatus("active");

        ReflectionTestUtils.setField(usuarioModelo, "id", 1L);

        Mockito.when(repository.findById(idUsuario)).thenReturn(Optional.of(usuarioModelo));

        UsuarioFichaDTO resultado = service.encontrarUsuario(idUsuario);

        assertNotNull(resultado);
        assertEquals("Vegetal123", resultado.getUsername());
        assertEquals("Vegeta", resultado.getNome());
        assertEquals("vegeta@sayajin.com", resultado.getEmail());
        assertEquals("Japão", resultado.getPais());
        assertEquals("", resultado.getFotoPerfil());
        assertEquals("Sou o grande Vegetaaaaaa", resultado.getBio());
        assertEquals("user", resultado.getRole());
        assertEquals("active", resultado.getStatus());

    }

    @Test
    void buscaUsuarioPorNome () {

        String termo = "goku";

        Usuario u1 = new Usuario();
        ReflectionTestUtils.setField(u1, "id", 1L);
        u1.setNickName("GokuSuper");
        u1.setNome("Son Goku");

        Usuario u2 = new Usuario();
        ReflectionTestUtils.setField(u2, "id", 2L);
        u2.setNickName("GokuSsb");
        u2.setNome("Goku Blue");

        List<Usuario> listaSimulada = List.of(u1, u2);

        Mockito.when(repository.findByUsernameContainingIgnoreCaseOrNomeContainingIgnoreCase(termo, termo))
           .thenReturn(listaSimulada);

        List<UsuarioPublicDTO> resultado = service.buscarPorUsernameOuNome(termo);

        assertNotNull(resultado);
        assertEquals(2, resultado.size()); // Garante que retornou a quantidade certa
        
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("GokuSuper", resultado.get(0).getUsername());
        assertEquals("Son Goku", resultado.get(0).getNome());

        verify(repository, times(1))
            .findByUsernameContainingIgnoreCaseOrNomeContainingIgnoreCase(termo, termo);
        }

}
