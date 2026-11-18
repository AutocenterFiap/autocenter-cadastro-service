package br.com.autocenterfiap.cadastro.veiculo.adapter.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.autocenterfiap.cadastro.veiculo.domain.exception.*;

import br.com.autocenterfiap.cadastro.comum.erro.ErroResposta;

@Slf4j
@RestControllerAdvice(basePackages = "br.com.autocenterfiap.cadastro.veiculo")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VeiculoExceptionHandler {

    @ExceptionHandler({
        RenavamJaCadastradoException.class,
        PlacaJaCadastradaException.class,
        ChassiJaCadastradoException.class
    })
    public ResponseEntity<ErroResposta> handleConflitoDeDados(Exception ex, HttpServletRequest request) {
        log.warn("Conflito de dados de veículo na URI={}: {}", request.getRequestURI(), ex.getMessage());
        ErroResposta erro = new ErroResposta(HttpStatus.CONFLICT.value(), "Conflito de Dados", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler({
        ChassiInvalidoException.class,
        RenavamInvalidoException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<ErroResposta> handleDadoInvalido(Exception ex, HttpServletRequest request) {
        log.warn("Dado inválido de veículo na URI={}: {}", request.getRequestURI(), ex.getMessage());
        ErroResposta erro = new ErroResposta(HttpStatus.BAD_REQUEST.value(), "Erro de Validação", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(VeiculoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> handleVeiculoNaoEncontrado(VeiculoNaoEncontradoException ex, HttpServletRequest request) {
        log.warn("Veículo não encontrado na URI={}: {}", request.getRequestURI(), ex.getMessage());
        ErroResposta erro = new ErroResposta(HttpStatus.NOT_FOUND.value(), "Recurso Não Encontrado", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}
