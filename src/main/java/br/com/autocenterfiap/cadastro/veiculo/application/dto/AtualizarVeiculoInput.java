package br.com.autocenterfiap.cadastro.veiculo.application.dto;

import br.com.autocenterfiap.cadastro.veiculo.domain.enums.CategoriaVeiculo;
import br.com.autocenterfiap.cadastro.veiculo.domain.enums.TipoCombustivel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtualizarVeiculoInput {
    private String placa;
    private String chassi;
    private String renavam;
    private String marca;
    private String modelo;
    private Integer anoFabricacao;
    private Integer anoModelo;
    private String cor;
    private Long quilometragem;
    private TipoCombustivel tipoCombustivel;
    private CategoriaVeiculo categoriaVeiculo;
}
