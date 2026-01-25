package br.com.arthur.api_wildex.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.arthur.api_wildex.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    /*
     * Chave secreta usada para assinar o token
     * 
     * IMPORTANTE:
     * - nunca versionar isso em projeto real
     * - depois moveremos para application.yml
     */
    private static final String SECRET =
        "minha-chave-super-secreta-com-no-minimo-32-bytes";

    /*
     * Tempo de expiração do token
     * Aqui: 1 hora
     */
    private static final long EXPIRATION_TIME =
        1000 * 60 * 60;

    /*
     * Gera um JWT a partir de um usuário autenticado
     */
    public String gerarToken(Usuario usuario) {

        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.builder()
            // "quem é o dono do token"
            .setSubject(usuario.getUsername())

            // dados extras
            .claim("role", usuario.getRole())

            // quando foi criado
            .setIssuedAt(new Date())

            // quando expira
            .setExpiration(
                new Date(System.currentTimeMillis() + EXPIRATION_TIME)
            )

            // assinatura
            .signWith(key, SignatureAlgorithm.HS256)

            // gera o token final
            .compact();
    }

    /*
     * Valida se o token é válido e não expirou
     */
    public boolean tokenValido(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Extrai o username do token
     */
    public String getUsername(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }
}
