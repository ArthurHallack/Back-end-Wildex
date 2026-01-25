package br.com.arthur.api_wildex.security.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import br.com.arthur.api_wildex.security.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1️⃣ Ler o header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2️⃣ Se não existe token, o filtro NÃO FAZ NADA
        // Ele apenas deixa a requisição seguir
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3️⃣ Extrair o token (remove o "Bearer ")
        String token = authHeader.substring(7);

        // 4️⃣ Validar o token
        // Se for inválido ou expirado, NÃO autentica
        if (!jwtService.tokenValido(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5️⃣ Extrair o username do token
        String username = jwtService.getUsername(token);

        // 6️⃣ Criar o objeto Authentication
        // Note:
        // - Não tem senha
        // - Authorities vazias (ou você pode carregar roles)
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()
                );

        // 7️⃣ Colocar no SecurityContext
        // Aqui o Spring passa a considerar o usuário autenticado
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // 8️⃣ Sempre continuar a cadeia
        filterChain.doFilter(request, response);
    }
}