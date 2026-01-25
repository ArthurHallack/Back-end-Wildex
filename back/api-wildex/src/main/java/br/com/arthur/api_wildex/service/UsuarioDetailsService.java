package br.com.arthur.api_wildex.service;

import org.springframework.stereotype.Service;

import br.com.arthur.api_wildex.repositories.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public UsuarioDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return repository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado")
            );
    }
}

