package br.com.autocenterfiap.cadastro.comum.erro;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Corpo padrão das respostas de erro")
public class ErroResposta {

    @Schema(description = "Momento do erro", example = "2026-11-17T12:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "Código HTTP", example = "404")
    private Integer status;
    @Schema(description = "Tipo do erro", example = "Not Found")
    private String erro;
    @Schema(description = "Mensagem do erro", example = "Cliente nao encontrado: 99")
    private String mensagem;
    @Schema(description = "Caminho da requisição", example = "/v1/clientes/99")
    private String caminho;

    public ErroResposta(Integer status, String erro, String mensagem, String caminho) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.caminho = caminho;
    }
}
