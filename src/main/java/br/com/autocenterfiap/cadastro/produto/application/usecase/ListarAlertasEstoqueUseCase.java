package br.com.autocenterfiap.cadastro.produto.application.usecase;

import br.com.autocenterfiap.cadastro.produto.application.dto.ProdutoOutput;
import br.com.autocenterfiap.cadastro.produto.application.mapper.ProdutoApplicationMapper;
import br.com.autocenterfiap.cadastro.produto.application.port.ProdutoRepositoryPort;
import br.com.autocenterfiap.cadastro.produto.domain.entity.Produto;

import java.util.List;
import java.util.stream.Stream;

public class ListarAlertasEstoqueUseCase {

    private final ProdutoRepositoryPort repositoryPort;

    public ListarAlertasEstoqueUseCase(ProdutoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public List<ProdutoOutput> executar() {
        List<Produto> semEstoque = repositoryPort.findProdutosSemEstoque();
        List<Produto> estoqueBaixo = repositoryPort.findProdutosComEstoqueBaixo();

        return Stream.concat(semEstoque.stream(), estoqueBaixo.stream())
                .distinct()
                .map(ProdutoApplicationMapper::toOutput)
                .toList();
    }
}
