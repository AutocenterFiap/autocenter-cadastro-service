package br.com.autocenterfiap.cadastro.comum.erro;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ErroResponse(int status, String erro, String mensagem, LocalDateTime timestamp) {

    public static ResponseEntity<ErroResponse> de(HttpStatus status, String erro, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), erro, mensagem, LocalDateTime.now()));
    }
}
