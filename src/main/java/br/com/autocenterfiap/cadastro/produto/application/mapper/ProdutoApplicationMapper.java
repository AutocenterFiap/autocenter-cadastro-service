package br.com.autocenterfiap.cadastro.produto.application.mapper;

import br.com.autocenterfiap.cadastro.produto.application.dto.CriarProdutoInput;
import br.com.autocenterfiap.cadastro.produto.application.dto.ProdutoOutput;
import br.com.autocenterfiap.cadastro.produto.domain.entity.Produto;

public class ProdutoApplicationMapper {

    public static Produto toEntity(CriarProdutoInput input) {
        if (input == null) return null;

        return Produto.builder()
                .nome(input.getNome())
                .codigo(input.getCodigo())
                .descricao(input.getDescricao())
                .unidadeMedida(input.getUnidadeMedida())
                .precoUnitario(input.getPrecoUnitario())
                .quantidadeEstoque(input.getQuantidadeEstoque())
                .estoqueMinimo(input.getEstoqueMinimo())
                .categoria(input.getCategoria())
                .tipo(input.getTipo())
                .ativo(true)
                .build();
    }

    public static ProdutoOutput toOutput(Produto entity) {
        if (entity == null) return null;

        return ProdutoOutput.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .codigo(entity.getCodigo())
                .descricao(entity.getDescricao())
                .unidadeMedida(entity.getUnidadeMedida())
                .precoUnitario(entity.getPrecoUnitario())
                .quantidadeEstoque(entity.getQuantidadeEstoque())
                .estoqueMinimo(entity.getEstoqueMinimo())
                .categoria(entity.getCategoria())
                .tipo(entity.getTipo())
                .ativo(entity.getAtivo())
                .dataCriacao(entity.getDataCriacao())
                .dataUltimaAtualizacao(entity.getDataUltimaAtualizacao())
                .build();
    }
}
