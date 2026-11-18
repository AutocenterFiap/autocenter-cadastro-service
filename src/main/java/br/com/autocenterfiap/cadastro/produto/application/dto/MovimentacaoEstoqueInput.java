package br.com.autocenterfiap.cadastro.produto.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoEstoqueInput {
    private Integer quantidade;
}
