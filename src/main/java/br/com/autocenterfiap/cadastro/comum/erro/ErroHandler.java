package br.com.autocenterfiap.cadastro.comum.erro;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ErroHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> validacao(MethodArgumentNotValidException e, HttpServletRequest req) {
        var mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst().orElse("Requisicao invalida");
        return resposta(HttpStatus.BAD_REQUEST, "Requisicao Invalida", mensagem, req);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroResposta> corpoInvalido(Exception e, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST, "Requisicao Invalida", "Corpo ou parametro da requisicao invalido", req);
    }

    public static ResponseEntity<ErroResposta> resposta(HttpStatus status, String erro, String mensagem,
            HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(new ErroResposta(status.value(), erro, mensagem, req.getRequestURI()));
    }
}
