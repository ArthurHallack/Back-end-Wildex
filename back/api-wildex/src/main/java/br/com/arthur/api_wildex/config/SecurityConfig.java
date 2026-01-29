package br.com.arthur.api_wildex.config;

/*
 * IMPORTAÇÕES
 * Aqui estão as peças do Spring Security que vou conectar.
 * reler com frequencia pra não esquecer.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import br.com.arthur.api_wildex.security.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;

/*
 * @Configuration
 * Diz ao Spring:
 * "essa classe contém definições de beans"
 * 
 * O Spring SEMPRE lê classes @Configuration na inicialização
 */
@Configuration

/*
 * @EnableWebSecurity
 * Ativa o Spring Security no projeto
 * 
 * Sem isso:
 * - SecurityFilterChain não funciona
 * - autenticação não existe
 */
@EnableWebSecurity
public class SecurityConfig {

    /*
     * 🔐 SECURITY FILTER CHAIN
     *
     * Isso é o CORAÇÃO do Spring Security.
     * 
     * Toda requisição HTTP passa por essa cadeia de filtros
     * ANTES de chegar em qualquer Controller.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
            /*
             * CSRF (Cross-Site Request Forgery)
             * 
             * Para APIs REST stateless (JWT):
             * → normalmente DESLIGADO
             * 
             * Para aplicações com sessão + formulário:
             * → normalmente LIGADO
             */
            .csrf(csrf -> csrf.disable())


            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    // Aqui retorna 401 para qualquer requisição sem token ou token inválido
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        {
                          "status": 401,
                          "error": "Unauthorized",
                          "message": "Token inválido ou ausente"
                        }
                    """);
                })
            )

            /*
             * AUTORIZAÇÃO DAS ROTAS
             *
             * Aqui você define:
             * - quem pode acessar o quê
             */
            .authorizeHttpRequests(auth -> auth

                /*
                 * anyRequest().permitAll()
                 *
                 * Estado ATUAL do seu projeto:
                 * - todas as rotas públicas
                 * - nenhuma autenticação exigida
                 *
                 * Mais pra frente você vai trocar por:
                 * .requestMatchers("/auth/**").permitAll()
                 * .anyRequest().authenticated()
                 */
                .requestMatchers(HttpMethod.POST, "/usuario/logon").permitAll()
                .requestMatchers("/error").permitAll() // ADICIONE ISSO
                .anyRequest().authenticated()

            );
            /*
            * 🔗 REGISTRO DO JWT FILTER
            *
            * O JwtFilter roda ANTES do filtro padrão de login
            * garantindo que o usuário seja identificado
            * antes das regras de autorização
            */
            http.addFilterBefore(
                jwtFilter,
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
            );


        /*
         * Constrói e devolve a cadeia de filtros
         * que o Spring vai usar internamente
         */
        return http.build();
    }

    /*
    * 🔐 JWT FILTER
    *
    * Filtro responsável por:
    * - interceptar TODAS as requisições
    * - extrair o JWT do header Authorization
    * - validar o token
    * - reconstruir a autenticação no SecurityContext
    *
    * Ele NÃO decide permissão.
    * Ele apenas autentica (ou não).
    */
    @Bean
    public JwtFilter jwtFilter(br.com.arthur.api_wildex.security.JwtService jwtService) {
        return new JwtFilter(jwtService);
    }

    /*
     * 🔑 AUTHENTICATION MANAGER
     *
     * É o CARA que executa o login.
     *
     * Ele:
     * - recebe username + senha
     * - chama UserDetailsService
     * - usa PasswordEncoder
     * - valida tudo
     *
     * Você NÃO cria ele manualmente.
     * O Spring monta isso automaticamente.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        /*
         * O AuthenticationConfiguration já conhece:
         * - UserDetailsService
         * - PasswordEncoder
         * - SecurityFilterChain
         *
         * Ele monta o AuthenticationManager correto
         */
        return config.getAuthenticationManager();
    }

    /*
     * 🔐 PASSWORD ENCODER
     *
     * Responsável por:
     * - criptografar senha no cadastro
     * - comparar senha no login
     *
     * BCrypt:
     * - padrão da indústria
     * - seguro
     * - com salt embutido
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

