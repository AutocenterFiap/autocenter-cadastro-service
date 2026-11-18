package br.com.autocenterfiap.cadastro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.util.Date;

public final class TokensTeste {

    public static final String SEGREDO = "chave-de-teste";
    public static final String ISSUER = "Auto Center Fiap";

    private TokensTeste() {
    }

    public static String interno() {
        return "Bearer " + JWT.create().withIssuer(ISSUER).withSubject("admin").withClaim("tipo", "interno")
                .withExpiresAt(Date.from(Instant.now().plusSeconds(3600))).sign(Algorithm.HMAC256(SEGREDO));
    }

    public static String internoExpirado() {
        return "Bearer " + JWT.create().withIssuer(ISSUER).withSubject("admin").withClaim("tipo", "interno")
                .withExpiresAt(Date.from(Instant.now().minusSeconds(60))).sign(Algorithm.HMAC256(SEGREDO));
    }

    public static String cliente(long clienteId) {
        return "Bearer " + JWT.create().withIssuer(ISSUER).withSubject("11144477735").withClaim("tipo", "cliente")
                .withClaim("clienteId", clienteId).withExpiresAt(Date.from(Instant.now().plusSeconds(3600)))
                .sign(Algorithm.HMAC256(SEGREDO));
    }
}
